function stats = GenerateSeriesStatistics(series, grids, fields, processors, parameters, task)
  %GENERATESERIESFFT Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  forceGenerate             = false;
  imageTypes                = {'halftone', 'screen', 'contone', 'monotone'};
  halftoneOutput            = true;
  retinaOutput              = true;
  
  stats                     = [];
  
  try
    OUTPUT                  = evalin('caller', 'output');
    setOuput                = true;
    
    try if ~exist('series', 'var')  || isempty(series)
        series              = OUTPUT.Series; end;
    catch
      series                = struct;
    end
    
    try if ~exist('grids', 'var')   || isempty(grids)
        grids               = OUTPUT.Grids; end;
    end
    
    try if ~exist('fields', 'var')  || isempty(fields)
        fields              = OUTPUT.Fields; end;
    end
    
    try if ~exist('parameters', 'var')  || isempty(parameters)
        parameters          = OUTPUT.Parameters; end;
    end
    
    try if ~exist('processors', 'var')  || isempty(processors)
        processors          = OUTPUT.Processors; end;
    end
    
  catch err
    setOuput                = false;
  end
  
  try
    if ~exist('series', 'var') || ~isstruct(series) || ~isfield(series, 'SRF')
        series.SRF          = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
    end
  catch err
    debugStamp(err, 1);
  end
  
  
  try
    if ~exist('series', 'var') || ~isstruct(series) || ~isfield(series, 'PRF')
        series.PRF          = PatchSeriesProcessor.LoadData('PRF', 'PRFData');
    end
  catch err
    debugStamp(err, 1);
  end  
  
  setTask                   = exist('task', 'var') && isa(task, 'Grasppe.Occam.ProcessTask');
  
  fieldTable                = fields.Table;
  seriesRows                = grids.Halftone.Rows;
  seriesRange               = 1:seriesRows;
  
  halftoneRows              = grids.Halftone.Rows;
  screenRows                = grids.Screen.Rows;
  contoneRows               = grids.Contone.Rows;
  monotoneRows              = grids.Monotone.Rows;
  
  screenIdxs                = grids.Screen.Index;
  contoneIdxs               = grids.Contone.Index;
  monotoneIdxs              = grids.Monotone.Index;
  
  screenRefs                = grids.Screen.Reference;
  contoneRefs               = grids.Contone.Reference;
  monotoneRefs              = grids.Monotone.Reference;
  
  seriesTable               = series.Table;
  seriesParameters          = series.Parameters;
  seriesVariables           = series.Variables;
  
  halftonePaths             = series.Paths.Halftone;
  screenPaths               = series.Paths.Screen;
  contonePaths              = series.Paths.Contone;
  monotonePaths             = series.Paths.Monotone;
  
  seriesFFT                 = cell(seriesRows, 6);
  halftonePSRFTable          = cell(seriesRows, 2);
  screenPSRFTable            = halftonePSRFTable;
  contonePSRFTable           = halftonePSRFTable;
  monotonePSRFTable          = halftonePSRFTable;
  
  % srfsAvailable             = isstruct(series) && all(isfield(series, {'SRF', 'FFT'})) && isstruct(series.SRF);
  srfsAvailable             = isstruct(series) && isfield(series, 'SRF') && isstruct(series.SRF);
  halftoneSRFAvailable      = srfsAvailable && isfield(series.SRF, 'Halftone') && iscell(series.SRF.Halftone);
  screenSRFAvailable        = srfsAvailable && isfield(series.SRF, 'Screen')   && iscell(series.SRF.Screen);
  contoneSRFAvailable       = srfsAvailable && isfield(series.SRF, 'Contone')  && iscell(series.SRF.Contone);
  monotoneSRFAvailable      = srfsAvailable && isfield(series.SRF, 'Monotone') && iscell(series.SRF.Monotone);
  
  halftoneSRFs              = {};
  screenSRFs                = {};
  contoneSRFs               = {};
  monotoneSRFs              = {};
  
  if halftoneSRFAvailable,  halftoneSRFs  = series.SRF.Halftone;  end
  if screenSRFAvailable,    screenSRFs    = series.SRF.Screen;    end
  if contoneSRFAvailable,   contoneSRFs   = series.SRF.Contone;   end
  if monotoneSRFAvailable,  monotoneSRFs  = series.SRF.Monotone;  end
  
  %% Process FFT & SRF Data
  try
    parfor m = seriesRange
      
      if rem(m, 50)==0,
        dispf('Generating Series Statistics... %d of %d', m, seriesRows);
      end
      
      %% Outputs
      halftoneOutput            = true;
      screenOutput              = any(m==screenIdxs);
      contoneOutput             = any(m==contoneIdxs);
      monotoneOutput            = any(m==monotoneIdxs);
      
      halftoneIdx               = m;
      screenIdx                 = screenRefs(m);
      contoneIdx                = contoneRefs(m);
      monotoneIdx               = monotoneRefs(m);
      
      halftoneID                = seriesTable{m, 4};
      screenID                  = seriesTable{m, 3};
      contoneID                 = seriesTable{m, 5};
      monotoneID                = seriesTable{m, 6};
      
      %% SRF Data
      halftoneSRF               = halftoneSRFs(halftoneIdx, :);
      screentoneSRF             = screenSRFs(screenIdx, :);
      contoneSRF                = contoneSRFs(contoneIdx, :);
      monotoneSRF               = monotoneSRFs(monotoneIdx, :);
      
      %% Image & Retina Paths
      htIMGPaths                = halftonePaths(halftoneIdx,:);
      scIMGPaths                = screenPaths(screenIdx,:);
      ctIMGPaths                = contonePaths(contoneIdx,:);
      mtIMGPaths                = monotonePaths(monotoneIdx,:);
      
       
      % if ~all([htFIMGExists scFIMGPath ctFIMGExists mtFIMGExists])
      %   continue; end      
      
      %% Check that FFTData Exists (Screen, Contone, Monotone Only)
      [htFFTPath  htFFTExists]  = PatchSeriesProcessor.GetResourcePath('Halftone FFTData',  halftoneID, 'mat');
      [scFFTPath  scFFTExists]  = PatchSeriesProcessor.GetResourcePath('Screen FFTData',    screenID,   'mat');
      [ctFFTPath  ctFFTExists]  = PatchSeriesProcessor.GetResourcePath('Contone FFTData',   contoneID,  'mat');
      [mtFFTPath  mtFFTExists]  = PatchSeriesProcessor.GetResourcePath('Monotone FFTData',  monotoneID, 'mat');
      
      if ~all([scFFTExists ctFFTExists mtFFTExists]), continue; end
      
           
      %% Determine Band Parameters
      bandParameters            = {[], []};
      try
        variables               = seriesVariables(m);
        bandParameters{1}       = variables.Metrics.BandParameters(1);
        bandParameters{2}       = variables.Metrics.BandParameters(2);
      end
      
      
      %% Generate Halftone FFTData
      if ~htFFTExists
        [htFFTs htSRFs FIMG htIMGs] = PatchSeriesProcessor.ProcessImageFourier( ...
          halftonePaths(halftoneIdx,:), bandParameters);
        
        htFIMGs                   = generateFourierImages(FIMG);
        
        htSRFCheck                = isequal(halftoneSRF, htSRFs);
        
        if ~htSRFCheck
          try
            error('Grasppe:Series:UnexpectedError', 'Halftone SRFs are not matching?');
          catch err
            debugStamp(err, 1);
            rethrow(err);
          end
        end
      else
        htFFTs                  = loadData(htFFTPath);
      end
      
      %% Get Screen, Contone, Monotone FFTData
      scFFTs                    = loadData(scFFTPath);
      ctFFTs                    = loadData(ctFFTPath);
      mtFFTs                    = loadData(mtFFTPath);
      
      %% Get FFT Images
      [htFIMGPath   htFIMGExists  ] = PatchSeriesProcessor.GetResourcePath('Halftone FFTImage', halftoneID, 'png');
      [scFIMGPath   scFIMGExists  ] = PatchSeriesProcessor.GetResourcePath('Screen FFTImage',   screenID, 'png');
      [ctFIMGPath   ctFIMGExists  ] = PatchSeriesProcessor.GetResourcePath('Contone FFTImage',  contoneID, 'png');
      [mtFIMGPath   mtFIMGExists  ] = PatchSeriesProcessor.GetResourcePath('Monotone FFTImage', monotoneID, 'png');
      
      %% Get FFT Retina Images
      [htFRMGPath   htFRMGExists  ] = PatchSeriesProcessor.GetResourcePath('Halftone RetinaFFTImage', halftoneID, 'png');
      [scFRMGPath   scFRMGExists  ] = PatchSeriesProcessor.GetResourcePath('Screen RetinaFFTImage',   screenID, 'png');
      [ctFRMGPath   ctFRMGExists  ] = PatchSeriesProcessor.GetResourcePath('Contone RetinaFFTImage',  contoneID, 'png');
      [mtFRMGPath   mtFRMGExists  ] = PatchSeriesProcessor.GetResourcePath('Monotone RetinaFFTImage', monotoneID, 'png');
      
      
      scFIMGs = {}; ctFIMGs = {}; mtFIMGs = {};
      
      try
        scFIMGs{1}                = PatchSeriesProcessor.LoadImage('Screen FFTImage',         screenID);
        scFIMGs{2}                = PatchSeriesProcessor.LoadImage('Screen RetinaFFTImage',   screenID);
        
        ctFIMGs{1}                = PatchSeriesProcessor.LoadImage('Contone FFTImage',        contoneID);
        ctFIMGs{2}                = PatchSeriesProcessor.LoadImage('Contone RetinaFFTImage',  contoneID);
        
        mtFIMGs{1}                = PatchSeriesProcessor.LoadImage('Monotone FFTImage',       monotoneID);
        mtFIMGs{2}                = PatchSeriesProcessor.LoadImage('Monotone RetinaFFTImage', monotoneID);
        
      catch err
        disp(err);
        debugStamp(err);
        beep;
      end
      
      %% Generate Cross Product Data & Images
      
      htPFFTs = {}; scPFFTs = {}; ctPFFTs = {}; mtPFFTs = {};
      
      PFFPath = @(x) [x(1:end-4) '-Monotone' x(end-3:end)];
      %PFFPath = @(x) ['Monotone-' x];

      for q = 1:2
        htPFFTs{q}                    = htFFTs{q}.*mtFFTs{q};
        if screenOutput,  scPFFTs{q}  = scFFTs{q}.*mtFFTs{q}; end
        if contoneOutput, ctPFFTs{q}  = ctFFTs{q}.*mtFFTs{q}; end
        if monotoneOutput,mtPFFTs{q}  = mtFFTs{q}.*mtFFTs{q}; end
      end
      
      if halftoneOutput
        [htPFFT htPSRFs htPSRFIMG]  = PatchSeriesProcessor.ProcessImageFourier(htPFFTs, bandParameters);
        htPSRFIMG                   = generateFourierImages(htPSRFIMG);
        imwrite(htPSRFIMG{1}, PFFPath(htFIMGPath));
        imwrite(htPSRFIMG{2}, PFFPath(htFRMGPath));
        %saveData(PFFPath(htFFTPath), htPFFT);
        
        halftonePSRFTable(m, :)     = htPSRFs;
      end
      
      if screenOutput
        [scPFFT scPSRFs scPSRFIMG]  = PatchSeriesProcessor.ProcessImageFourier(scPFFTs, bandParameters);
        scPSRFIMG                   = generateFourierImages(scPSRFIMG);
        imwrite(scPSRFIMG{1}, PFFPath(scFIMGPath));
        imwrite(scPSRFIMG{2}, PFFPath(scFRMGPath));
        saveData(PFFPath(scFFTPath), scPFFT);
        
        screenPSRFTable(m,:)          = scPSRFs;
      end
      
      if contoneOutput
        [ctPFFT ctPSRFs ctPSRFIMG]  = PatchSeriesProcessor.ProcessImageFourier(ctPFFTs, bandParameters);
        ctPSRFIMG                   = generateFourierImages(ctPSRFIMG);
        imwrite(ctPSRFIMG{1}, PFFPath(ctFIMGPath));
        imwrite(ctPSRFIMG{2}, PFFPath(ctFRMGPath));
        saveData(PFFPath(ctFFTPath), ctPFFT);
        
        contonePSRFTable(m,:)       = ctPSRFs;
      end
      
      if monotoneOutput
        [mtPFFT mtPSRFs mtPSRFIMG]  = PatchSeriesProcessor.ProcessImageFourier(mtPFFTs, bandParameters);
        mtPSRFIMG                   = generateFourierImages(mtPSRFIMG);
        imwrite(mtPSRFIMG{1}, PFFPath(mtFIMGPath));
        imwrite(mtPSRFIMG{2}, PFFPath(mtFRMGPath));
        saveData(PFFPath(mtFFTPath), mtPFFT);
        
        monotonePSRFTable(m, :)     = mtPSRFs;
      end
              
      %% Generate Statistics
      
    end
  catch err
    debugStamp(err,1);
    beep;
  end
  
  screenPSRFTable               = screenPSRFTable(screenIdxs, :);
  contonePSRFTable              = contonePSRFTable(contoneIdxs, :);
  monotonePSRFTable             = monotonePSRFTable(monotoneIdxs, :);
  
  
  output                        = struct;
  output.PRF.Monotone.Halftone  = halftonePSRFTable;
  output.PRF.Monotone.Screen    = screenPSRFTable;
  output.PRF.Monotone.Contone   = contonePSRFTable;
  output.PRF.Monotone.Monotone  = monotonePSRFTable;
  
  if ~isfield(series, 'PRF') || ~isequal(output.PRF, series.PRF)
    PatchSeriesProcessor.SaveData(output, 'PRFData');
  end
    
  if setOuput || nargout==0
    OUTPUT.Series           = series;
    assignin('caller', 'output', OUTPUT);
  end
  
end

function IMG = generateFourierImages(FFT)
  
  if ~iscell(FFT), FFT  = {FFT}; end
  
  IMG                   = cell(size(FFT));
  
  unpadDim              = @(f,d) ceil(size(f,d)/4)+[1:size(f,d)/2];
  
  for m = 1:numel(FFT)
    fP                    = unpadDim(FFT{m},1);
    fQ                    = unpadDim(FFT{m},2);
    IMG{m}                = realImage(FFT{m}(fP, fQ));
  end
end

function img = forwardFFT(img)
  sP  = size(img,1);
  sQ  = size(img,2);
  nP  = 1-mod(sP,2);
  nQ  = 1-mod(sQ,2);
  fP  = ceil(2*(sP-nP));
  fQ  = ceil(2*(sQ-nQ));
  
  img = img(1:end-(nP),1:end-(nQ));
  img = fftshift(fft2(img, fP, fQ));
  
end

function img = realImage(img)
  if ~isreal(img)
    img       = real(log(1+abs(img)));
    fftMin    = min(img(:)); imageMax = max(img(:));
    img       = (img-fftMin) / (imageMax-fftMin);
  end
end


function data = loadData(pth)
  data = load(pth);
  data = data.data;
end

function saveData(pth, data)
  save(pth, 'data');
  Grasppe.ConRes.File.UpdateTimeStamp(pth);
end

function img = bandPlotFFT(img, fftData, fQ)
  
  persistent hFig
  
  import(eval(NS.CLASS));
  
  dataColumn = 2;
  renderer = 'opengl';
  
  try
    
    image1    = img;
      
    width     = size(image1,2);
    height    = size(image1,1);
        
    hFig  = figure('Visible', 'off', 'Position', [-1000 -1000 width height], 'HandleVisibility','callback', 'Renderer', 'painters');
        
    hAxis = axes('Parent', hFig);
        
    % [bFq fftData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(image1b));
        
    bFq = fftData(:,dataColumn);
    
    yR  = bFq/max(bFq(:));
    yR2 = (1-yR)*0.5;
    xR  = 1:numel(bFq);
    zR  = ones(size(xR));
    
    xF  = (7/2);
    xD  = 0.5 + floor((size(image1,2)/4));
    yD  = 0.5 + floor(size(image1,1)/2);
    
    yZ  = 3;
    yR  = (yR*100)-(max(yR(yZ:end)*100)/2);
    yR2 = (yR2*100)-(max(yR2(yZ:end)*100)/2);
    yM  = 0;
    yM2 = nanmean(yR2(yZ:end));
    yS  = 5;
    
    image2 = repmat(image1, [1, 1, 3]);
    
    cla(hAxis); hold(hAxis, 'on');
    imshow(image2, 'Parent', hAxis);
    truesize(hFig);
        
    lOp = {'Parent', hAxis, 'LineWidth', 1, 'linesmoothing','on'};
    
    yN = 1;
    x  = []; y = [];
    for m = 1:5:numel(xR)
      xv = [ 0 0]  + (xD+xR(m)*xF);
      if yN==1
        yv = [-35 35]  + yD + yM + yS;
        yN = 0;
      else
        yv = [-25 25]  + yD + yM + yS;
        yN = 1;
      end
      line(xv, yv, [0 0], 'Color', 'w', 'LineStyle', ':',   lOp{:}, 'LineWidth', 0.5);
    end
        
    plot(hAxis, xD+xR*xF, yD+yR2+yS, 'g', lOp{:}, 'Linewidth', 0.5);
        
    if isnumeric(fQ) && ~isempty(fQ)
      for m = fQ
        yv = [-35 35]  + yD + yM + yS;
        xv = [0 0] + xD + m*xF;
        xv2 = [0 0] + xD*2;
        line(xv, yv, [0 0], 'Color', 'r', lOp{:}, 'LineWidth', 1);
        
        text(mean(xv2), max(yv)+1, 0, [num2str(m,'%3.1f')], 'Parent', hAxis, 'HorizontalAlignment', 'center', 'VerticalAlignment', 'top', 'Color', 'g', 'FontSize', 8, 'FontWeight', 'bold');
      end
      %             end
      %
      %             if isnumeric(fQ) && ~isempty(fQ)
      fQ2 = fQ(1);
      for m = fQ2
        yv = [-20 20]  + yD + yM + yS;
        xv = [0 0] + xD + m*xF;
        xv2 = [0 0] + xD*2;
        line(xv, yv, [0 0], 'Color', 'r', lOp{:}, 'LineWidth', 1);
        zi = [-1:1]+floor(m);
        zv = max(bFq(zi));
        zs = max(fftData(zi,5));
        %zs2 = max(fqData(zi,6));
        
        tVal = regexprep(num2str(zv,'%3.2e'),'([\d\.]+)(e[+-])[0]?(\d+)','$1$2$3');
        tStd = num2str(zs, '%1.2f');
        %tStd = [num2str(zs, '%1.2f') ' ' num2str(zs2, '%1.2f')];
        
        text(mean(xv2), min(yv)-15, 0, [tVal ' (' tStd ')'], 'Parent', hAxis, 'HorizontalAlignment', 'center', 'VerticalAlignment', 'bottom', 'Color', 'g', 'FontSize', 8, 'FontWeight', 'bold');
        
      end
    end
        
    img = export_fig(hFig, '-native', '-a2', ['-' renderer]);
        
    delete(hAxis);
    delete(hFig);
    
  catch err
    debugStamp(err, 1);
    rethrow(err);
  end
end
