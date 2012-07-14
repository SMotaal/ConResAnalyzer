function [halftone stack] = ffScreening( contone, resolution, title, spi, lpi, dpi, angles, noise)
% try
  
  isValue  = @(x) isscalar(x) && isnumeric(x);
  isString = @(x) ischar(x);
  
  if ischar(contone)
    T = tic; status(sprintf('Loading %s', contone));
    Image = loadImage(contone);
    fprintf('Loading %s Complete. ', Image.Path); toc(T);
  else
    
    if ~exist('resolution', 'var') || ~isValue(resolution), ...
        spi = 1270; end
    
    if ~exist('title', 'var') || ~isValue(title),  ...
        title = 'Sample'; end
    
    Image.Data    = im2uint8(contone);
    Image.Info    = struct('Filename', title);
    Image.PPI     = resolution;
    Image.Width   = size(contone, 2);
    Image.Height  = size(contone, 1);
    Image.Colors  = size(contone, 3);
  end
  
  T = tic; status(sprintf('Preprocessing %s', Image.Info.Filename));
  
  defaults = {2450, 175, 2*175, [15, 75, 0, 45], 0};
  
  if ~exist('spi',    'var')  || ~isValue(spi),     spi     = defaults{1}; end
  if ~exist('lpi',    'var')  || ~isValue(lpi),     lpi     = defaults{2}; end
  if ~exist('dpi',    'var')  || ~isValue(dpi),     dpi     = defaults{3}; end
  if ~exist('angles', 'var')  || ~isValue(angles),  angles  = defaults{4}; end
  if ~exist('noise',  'var')  || ~isValue(noise),   noise   = defaults{5}; end
  
  %% Define Screen
  Screen.SPI    = spi;    % Addressibility
  Screen.LPI    = lpi;    % Lines-Per-Inch
  Screen.DPI    = dpi;    % Resolution      Screen.LPI * 2;
  Screen.Angle  = angles; % Screen angles   [0, ...]
  Screen.Noise  = noise;  % Noise Level     4/100;
  Screen.Pitch  = Screen.SPI / Screen.LPI;
  
  %% Process Input
  Input.PPI     = 1;
  Input.Width   = Image.Width  / Image.PPI * Input.PPI;
  Input.Height  = Image.Height / Image.PPI * Input.PPI;
  Input.Data    = Image.Data;
  Input.Colors  = size(Input.Data, 3);
  
  %% Define Output
  Output.PPI    = Screen.SPI;
  Output.Width  = round(Input.Width  / Input.PPI * Output.PPI);
  Output.Height = round(Input.Height  / Input.PPI * Output.PPI);
  Output.Data   = zeros(Output.Height, Output.Width, Image.Colors)~=0;
  
  %% Define Imaging Plane
  Raster.PPI    = Screen.DPI;
  Raster.Width  = Input.Width  / Input.PPI * Raster.PPI;
  Raster.Height = Input.Height / Input.PPI * Raster.PPI;
  Raster.Colors = Input.Colors; %size(Raster.Data, 3);
  % Raster.Data   = zeros(Raster.Width, Raster.Height, Raster.Colors)>0.5;
  
  status(sprintf('Resizing Input Stream: Image @ %d x %d ==> Raster @ %d x %d pixels', Image.Width, Image.Height,  Raster.Width, Raster.Height));
  Raster.Data   = imresize(Input.Data, [Raster.Height Raster.Width], 'nearest');
  
  %% Separate Channels
  for c = 1:Raster.Colors
    status(sprintf('Splitting Raster Stream: Channel %d of %d', c, Raster.Colors));
    Raster.Channels(c).Data = squeeze(Raster.Data(:,:,c));
  end
  
  fprintf('Preprocessing Complete. '); toc(T);
  
  %% Generate Rastern Channel Halftones
  
  T = tic; status(sprintf('Processing %s', Image.Info.Filename));
  
  zeroCell = zeros(Screen.Pitch, Screen.Pitch)~=0;
  
  zeroChannel = ones(Output.Height, Output.Width)~=0;
  
  l   = ceil(Screen.Pitch/6);
  s   = Screen.Pitch + 2*l;
  t   = Screen.Angle; %[15, 75, 0, 45]; %[0 15 345];
  nf  = Screen.Noise;
  
  for c = 1:Raster.Colors
    
    status(sprintf('Screening Raster: Channel %d of %d (%d%%%%)', c, Raster.Colors, 0));
    
    T2 = tic;
    
    rasterData      = Raster.Channels(c).Data;
    
    Channel.Raster  = rasterData; % Raster.Channels(c).Data;
    
    if t(c)~=0
      ns = size(Channel.Raster);
      nr = uint8(ones(max(ns)+50, max(ns)+50)) * max(rasterData(:));
      %nr = ones(max(ns)+2, max(ns)+2) .* max(rasterData(:));
      nl = floor((size(nr) - ns)/2);
      
      nr(nl(1)+1:nl(1)+ns(1), nl(2)+1:nl(2)+ns(2)) = Channel.Raster;
      
      status(sprintf('Screening Raster: Channel %d of %d (Rotating %sº)', c, Raster.Colors, num2str(t(c))));
      
      Channel.Raster  = imrotate(nr, t(c));
    end
    
    Channel.PPI     = Screen.DPI;
    Channel.Width   = size(Channel.Raster,2); %/ Raster.PPI * Channel.PPI;
    Channel.Height  = size(Channel.Raster,1); %/ Raster.PPI * Channel.PPI;
    
    Channel.Pitch   = Raster.PPI / Screen.LPI;
    
    Channel.Columns = Channel.Width  / Channel.Pitch; %Input.Width  / Input.PPI * Screen.LPI;
    Channel.Rows    = Channel.Height / Channel.Pitch; %Input.Height / Input.PPI * Screen.LPI;
    
    if t(c)~=0
      
      Channel.Output  = zeros(ceil([Channel.Width Channel.Height] ./Channel.PPI.*Output.PPI))~=0;
    else
      Channel.Output  = zeroChannel;
    end
    
    try
      cprog = 0;
      ceta  = 0;
      cstep = round((1000+Channel.Columns)/1000);
      T3    = tic;
      
      for m = 1:Channel.Columns
        for n = 1:Channel.Rows
          try
            q = round(Channel.Pitch);
            x = (m-1)*q + 1;
            y = (n-1)*q + 1;
            d = Channel.Raster(y:y+q-1, x:x+q-1);
            v = mean(d(:)) / 255;
            if v>0 && v<1
              nv  = v + (rand*nf-nf/2);
              if nv>0 && nv<1, v = nv; end
            end
            r = round(v*Screen.Pitch);
            try
              r = round(r * s/Screen.Pitch);
              p = ceil((s - r) / 2);
              h = imcircle(r);
              h = padarray(h, [p p], 0);
              h = h(l+1:end-l, l+1:end-l)>0.5;
            catch err
              h = zeroCell;
            end
            
            o = h(round(1:Screen.Pitch), round(1:Screen.Pitch));
            
            cx = round((m-1)*Screen.Pitch + [1:Screen.Pitch]);
            cy = round((n-1)*Screen.Pitch + [1:Screen.Pitch]);
            % imshow(o);
            Channel.Output(cy, cx) = o;
          catch err
            disp(err);
          end
        end
        
        oprog = m*100/Channel.Columns;
        if round(oprog*cstep)~=round(cprog*cstep) %mod(round(oprog), 5)==0 || oprog>99
          cprog = oprog;
          
          if cprog < 2
            status(sprintf('Screening Raster: Channel %d of %d (%d%%%%)', c, Raster.Colors, round(cprog)));
          else
            ctime = toc(T3);
            oeta  = ctime/cprog * (100-cprog);
            
            if oeta<ceta || oeta-ceta > 2
              ceta = oeta;
            end
            
            status(sprintf('Screening Raster: Channel %d of %d (%d%%%%) ~ %d seconds', c, Raster.Colors, round(cprog), round(ceta * 1.25)));
            
            ceta = oeta;
          end
        end
        
      end
      
      
      if t(c)~=0
        g  = imrotate(Channel.Output, -t(c));
        
        status(sprintf('Screening Raster: Channel %d of %d (Rotating -%sº)', c, Raster.Colors, num2str(t(c))));
        
        Channel.XCrop   = floor((size(g,2) - size(Output.Data,2))/2 + [1:size(Output.Data,2)]);
        Channel.YCrop   = floor((size(g,1) - size(Output.Data,1))/2 + [1:size(Output.Data,1)]);
        Channel.Output  = g(Channel.YCrop, Channel.XCrop);
      end
      
      %catch err
    end
    ox2 = min(size(Output.Data,2), size(Channel.Output,2));
    oxd = size(Output.Data,2)-size(Channel.Output,2);
    oy2 = min(size(Output.Data,1), size(Channel.Output,1));
    oyd = size(Output.Data,1)-size(Channel.Output,1);
    
    status(sprintf('Screening Raster: Channel %d of %d (...)', c, Raster.Colors));
    Output.Data(1:oy2, 1:ox2, c) = Channel.Output(1:oy2, 1:ox2)>0.5;
    
    status(sprintf('Screening Raster: Channel %d of %d (Done)', c, Raster.Colors));
    fprintf('Generated channel %d %d(%d) x %d(%d) at %1.2fº. ', c, ox2, oxd, oy2, oyd, t(c)); toc(T2);
    
  end
  
  fprintf('Screening Complete. '); toc(T); %beep;
  
  halftone    = Output.Data;
  logicalData = halftone>0.5;
  
  %     imdisc = Image.Path;
  %     try
  %       [impath imname imext] = fileparts(Image.Path);
  %       imdpi     = Image.PPI;
  %       imlpi     = Screen.LPI;
  %       imspi     = Screen.SPI;
  %       imangles  = sprintf('%dº ', t);
  %       mstamp    = [mfilename ' (' num2str(mfilerev) ')'];
  %       imdisc    = sprintf('%s %1.1f dpi screened using %s at %1.1f lpi / %1.1f spi', ...
  %         imname, imdpi, mstamp, imlpi, imspi);
  %     end
  %
  %     prefix    = imname; %'output';
  %     sequence  = 1;
  %     suffix    = '.tif';
  %
  %     filename  = [prefix suffix];
  %     fileno    = 0;
  %
  %     outpath  = @(x) fullfile('./Output/', x);
  %
  %     while exist(outpath(filename),'file') > 0
  %       fileno = fileno+1;
  %       filename = [prefix int2str(fileno) suffix];
  %     end
  %
  %
  %     %% Export Composite
  %
  %     T = tic; status(sprintf('Preparing Composite Output: %s', imdisc));
  %
  %     imageWidth  = size(Output.Data,2);
  %     imageHeight = size(Output.Data,1);
  %
  %     if ByteSize(Output.Data) > 2^32
  %       if imageWidth > imageHeight
  %         newWidth  = 11585;
  %         newHeight = ceil(newWidth / imageWidth * imageHeight);
  %       elseif imageWidth < imageHeight
  %         newHeight = 11585;
  %         newWidth  = ceil(newHeight / imageHeight * imageWidth);
  %       else
  %         newWidth  = 11585;
  %         newHeight = 11585;
  %       end
  %
  %       imageData   = imresize(im2uint8(Output.Data), [newHeight newWidth]);
  %       imageScale  = imageWidth/newWidth;
  %     else
  %       imageData   = im2uint8(Output.Data);
  %       imageScale  = 1;
  %     end
  %
  %     filename    = [prefix int2str(fileno) suffix];
  %
  %     T = tic; status(sprintf('Writing Output: %s <== %s', filename, imdisc));
  %
  %     imwrite(imageData, outpath(filename), 'Resolution', Screen.SPI * [1 1] / imageScale, ...
  %       'Description', imdisc, 'Compression', 'lzw'); %beep;
  %
  %     fprintf('Saving %s Complete. ', filename); toc(T);
  %
  %     %% Export Separations
  %     T = tic; status(sprintf('Preparing Separated Output: %s', imdisc));
  %     logicalData = Output.Data>0.5;
  %
  %     for c = 1:size(logicalData,3)
  %       filename = [prefix int2str(fileno) '.' int2str(c) suffix];
  %
  %       T = tic; status(sprintf('Writing Output: %s <== %s', filename, imdisc));
  %
  %       imwrite(~logicalData(:,:,c), outpath(filename), 'Resolution', Screen.SPI * [1 1], ...
  %         'Description', imdisc, 'Compression', 'ccitt'); %beep;
  %
  %       fprintf('Saving %s Complete. ', filename); toc(T);
  %     end
  %
  %     try
  %       if showPreview
  %         imshow(Output.Data); %imresize(Output.Data, 0.5));
  %       end
  %     end
  %
  %     status('');
  %   end
  
  
  if nargout >0
    try stack.Screen  = Screen;       end
    try stack.Image   = Image;        end
    try stack.Input   = Input;        end
    try stack.Raster  = Raster;       end
    try stack.Output  = Output;       end
    try stack.Bitmap  = logicalData;  end
    % try stack.Preview = imageData;    end
  end
  
% end

end

function Image = loadImage(imagePath)
%% Load Image
if exist('imagePath', 'var')
  if exist(imagePath, 'file')>0
    Image.Path  = imagePath;
  else
    error('Grasppe:Screening:FileIO:NotFound', 'Could not process %s. The file does not exist.', toString(Image.Path));
  end
else
  Image.Path  = 'roman16_s300dpi_f39.tif'; %'~/Documents/Data/SCIDS/01348_lavander_225dpi_f39.tif';
end

Image.Data    = im2uint8(imread(Image.Path));
Image.Info    = imfinfo(Image.Path);
Image.PPI     = (Image.Info.XResolution==Image.Info.YResolution) * Image.Info.XResolution;

try
  switch lower(Image.Info.ResolutionUnit)
    case 'meter'
      Image.PPI = Image.PPI * 0.0254;
  end
end

Image.Width   = Image.Info.Width;
Image.Height  = Image.Info.Height;
try
  Image.Colors  = Image.Info.SamplesPerPixel;
catch
  try
    if ndims(Image.Data) == 2
      Image.Colors= 1;
    else
      Image.Colors= size(Image.Data, 3);
    end
  catch err
    disp(err);
  end
end
end
