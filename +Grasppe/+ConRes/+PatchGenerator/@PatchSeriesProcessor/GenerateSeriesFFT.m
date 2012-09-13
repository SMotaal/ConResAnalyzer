function series = GenerateSeriesFFT(series, grids, fields, processors, parameters, task)
  %GENERATESERIESFFT Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  forceGenerate             = false;
  imageTypes                = {'halftone', 'screen', 'contone', 'monotone'};
  halftoneOutput            = true;
  retinaOutput              = true;
  
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
      series.SRF            = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
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
  %   halftoneFFTData           = cell(seriesRows, 2);
  %   screenFFTData             = halftoneFFTData; % cell(seriesRows, 2);
  %   contoneFFTData            = halftoneFFTData; % cell(seriesRows, 2);
  %   monotoneFFTData           = halftoneFFTData; % cell(seriesRows, 2);
  
  halftoneSRFTable          = cell(seriesRows, 2);
  screenSRFTable            = halftoneSRFTable;
  contoneSRFTable           = halftoneSRFTable;
  monotoneSRFTable          = halftoneSRFTable;
  
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
      
      fdtPath                 = [];
      
      if rem(m, 50)==0,
        dispf('Generating Series FFT... %d of %d', m, seriesRows);
      end
      
      %% Outputs
      halftoneOutput          = true;
      screenOutput            = any(m==screenIdxs);
      contoneOutput           = any(m==contoneIdxs);
      monotoneOutput          = any(m==monotoneIdxs);
      
      halftoneIdx             = m;
      screenIdx               = screenRefs(m);
      contoneIdx              = contoneRefs(m);
      monotoneIdx             = monotoneRefs(m);
      
      halftoneID              = seriesTable{m, 4};
      screenID                = seriesTable{m, 3};
      contoneID               = seriesTable{m, 5};
      monotoneID              = seriesTable{m, 6};
      
      
      bandParameters          = {[], []};
      try
        variables             = seriesVariables(m);
        bandParameters{1}     = variables.Metrics.BandParameters(1);
        bandParameters{2}     = variables.Metrics.BandParameters(2);
      end
      
      
      if halftoneOutput
        SRF                   = {};
        fftExists             = false;
        
        if halftoneSRFAvailable
          try SRF             = halftoneSRFs(halftoneIdx, :); end
          [fftPath fftExists] = PatchSeriesProcessor.GetResourcePath('Halftone FFTImage', halftoneID, 'png');
        end
        
        % [fdtPath fdtExists] = PatchSeriesProcessor.GetResourcePath('Halftone FFTData', halftoneID, 'mat');
        
        if ~(halftoneSRFAvailable && fftExists && ~isempty(SRF))
          % [FFT SRF]           = processImageFourier(halftonePaths(halftoneIdx,:), bandParameters);
          [FFT SRF FIMG]      = PatchSeriesProcessor.ProcessImageFourier( ...
            halftonePaths(halftoneIdx,:), bandParameters); % debugStamp(sprintf('Trying to save halftone FFT (%s)', halftoneID), 5);
          
          IMG                 = generateFourierImages(FIMG);
          
          PatchSeriesProcessor.SaveImage(IMG{1}, 'Halftone FFTImage', halftoneID);
          PatchSeriesProcessor.SaveImage(IMG{2}, 'Halftone RetinaFFTImage', halftoneID);
          
          % halftoneFFT         = FFT;
          % halftoneFFTData(m,:)= screenFFT;
        end
        
        halftoneSRF           = SRF;
        halftoneSRFTable(m,:) = halftoneSRF;
        
      end
      
      if screenOutput
        SRF                   = {};
        fftExists             = false;
        
        if screenSRFAvailable
          try SRF             = screenSRFs(screenIdx, :); end
          [fftPath fftExists] = PatchSeriesProcessor.GetResourcePath('Screen FFTImage', screenID, 'png');
        end
        [fdtPath fdtExists] = PatchSeriesProcessor.GetResourcePath('Screen FFTData', screenID, 'mat');
        
        if ~(screenSRFAvailable && fftExists && ~isempty(SRF) && fdtExists)
          [FFT SRF FIMG]      = PatchSeriesProcessor.ProcessImageFourier( ...
            screenPaths(screenIdx,:), bandParameters);  % debugStamp(sprintf('Trying to save screen FFT (%s)', screenID), 5);
          
          saveData(fdtPath, FFT);
          
          IMG                 = generateFourierImages(FIMG);
          
          PatchSeriesProcessor.SaveImage(IMG{1}, 'Screen FFTImage', screenID);
          PatchSeriesProcessor.SaveImage(IMG{2}, 'Screen RetinaFFTImage', screenID);
          
          % screenFFT           = FFT;
          % screenFFTData(m,:)  = screenFFT;
        end
        
        screenSRF             = SRF;
        screenSRFTable(m, :)  = screenSRF;
        
      end
      
      if contoneOutput
        SRF                   = {};
        fftExists             = false;
        
        if contoneSRFAvailable
          try SRF             = contoneSRFs(contoneIdx, :); end
          [fftPath fftExists] = PatchSeriesProcessor.GetResourcePath('Contone FFTImage', contoneID, 'png');
        end
        
        [fdtPath fdtExists] = PatchSeriesProcessor.GetResourcePath('Contone FFTData', contoneID, 'mat');
        
        if ~(contoneSRFAvailable && fftExists && ~isempty(SRF) && fdtExists)
          %[FFT SRF]           = processImageFourier(contonePaths(contoneIdx,:), bandParameters);
          [FFT SRF FIMG]      = PatchSeriesProcessor.ProcessImageFourier( ...
            contonePaths(contoneIdx,:), bandParameters);  % debugStamp(sprintf('Trying to save contone FFT (%s)', contoneID), 5);
          
          saveData(fdtPath, FFT);
          
          IMG                 = generateFourierImages(FIMG);
          
          PatchSeriesProcessor.SaveImage(IMG{1}, 'Contone FFTImage', contoneID);
          PatchSeriesProcessor.SaveImage(IMG{2}, 'Contone RetinaFFTImage', contoneID);
          
          % contoneFFT          = FFT;
          % contoneFFTData(m,:) = FFT;
        end
        contoneSRF            = SRF;
        contoneSRFTable(m, :) = contoneSRF;
      end
      
      if monotoneOutput
        SRF                   = {};
        fftExists             = false;
        
        if monotoneSRFAvailable
          try SRF             = monotoneSRFs(monotoneIdx, :); end
          [fftPath fftExists] = PatchSeriesProcessor.GetResourcePath('Monotone FFTImage', monotoneID, 'png');
        end
        
        [fdtPath fdtExists] = PatchSeriesProcessor.GetResourcePath('Monotone FFTData', monotoneID, 'mat');
        
        if ~(monotoneSRFAvailable && fftExists && ~isempty(SRF)  && fdtExists)
          [FFT SRF FIMG]      = PatchSeriesProcessor.ProcessImageFourier( ...
            monotonePaths(monotoneIdx,:), bandParameters); % debugStamp(sprintf('Trying to save monotone FFT (%s)', monotoneID), 5);
          
          saveData(fdtPath, FFT);
          
          IMG                 = generateFourierImages(FIMG);
          
          PatchSeriesProcessor.SaveImage(IMG{1}, 'Monotone FFTImage', monotoneID);
          PatchSeriesProcessor.SaveImage(IMG{2}, 'Monotone RetinaFFTImage', monotoneID);
          
          % monotoneFFT         = FFT;
          % monotoneFFTData(m,:)= FFT;
        end
        
        monotoneSRF           = SRF;
        monotoneSRFTable(m,:) = monotoneSRF;
      end
      
      
      
    end
  catch err
    debugStamp(err,1);
    beep;
  end
  
  screenSRFTable              = screenSRFTable(screenIdxs, :);
  contoneSRFTable             = contoneSRFTable(contoneIdxs, :);
  monotoneSRFTable            = monotoneSRFTable(monotoneIdxs, :);
  
  
  output                      = struct;
  output.SRF.Halftone         = halftoneSRFTable;
  output.SRF.Screen           = screenSRFTable;
  output.SRF.Contone          = contoneSRFTable;
  output.SRF.Monotone         = monotoneSRFTable;
  
  if ~isfield(series, 'SRF') || ~isequal(output.SRF, series.SRF)
    PatchSeriesProcessor.SaveData(output, 'SRFData');
    % series.SRF                = output.SRF;
  end
    
  if setOuput || nargout==0
    OUTPUT.Series           = series;
    assignin('caller', 'output', OUTPUT);
  end
  
end

function saveData(pth, data)
  save(pth, 'data');
  Grasppe.ConRes.File.UpdateTimeStamp(pth);
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

function [FFT SRF]  = processImageFourier(imagePaths, bandParameters)
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  try
    
    patchImage            = PatchSeriesProcessor.LoadImage(imagePaths{1}); %'screen', screenID);
    retinaImage           = PatchSeriesProcessor.LoadImage(imagePaths{2}); %'screen', screenID);
    
    patchImageFFT         = forwardFFT(patchImage);
    retinaImageFFT        = forwardFFT(retinaImage);
    
    % unpadDim              = @(f,d) ceil(size(f,d)/4)+[1:size(f,d)/2];
    % fP                    = unpadDim(patchImageFFT,1);
    % fQ                    = unpadDim(patchImageFFT,2);
    % FFT                   = {patchImageFFT(fP, fQ), retinaImageFFT(fP, fQ)}; %patchFFTData, retinaFFTData};
    % FFT                   = {patchImageFFT, retinaImageFFT}; %patchFFTData, retinaFFTData};
    
    patchImageSRF         = Grasppe.Kit.ConRes.CalculateBandIntensity(patchImageFFT,  bandParameters{:});
    retinaImageSRF        = Grasppe.Kit.ConRes.CalculateBandIntensity(retinaImageFFT, bandParameters{:});
    
    SRF                   = {patchImageSRF, retinaImageSRF};
    
  catch err
    debugStamp(err, 1);
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

function img = bandPlotFFT(img)
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  persistent  fxBusy;
  
  dataColumn = 2;
  
  renderer = 'opengl';
  
  if isempty(img)
    fxBusy = false;
    return;
  end
  
  if isequal(fxBusy, true)
    img = [];
    return;
  end
  
  try
    
    fxBusy=true;
    DBG.dispdbg('Generating Image...');
    R=tic;
    try
      
      image1 = img;
      
      if size(image1,3) > 1
        image1 = image1(:,:,1);
        DBG.dispdbg('Flattening Image...');
      end
      
      image1b   = image1;
      
      image1    = realImage(image1);
      
      width     = size(image1,2);
      height    = size(image1,1);
      
      if isequal(PatchSeriesProcessor.PLOTFFT, true)
        
        hFig  = figure('Visible', 'off', 'Position', [-1000 -1000 width height], 'HandleVisibility','callback', 'Renderer', 'painters');
        
        hAxis = axes('Parent', hFig);
        
        DBG.dispdbg('Generating Plot...');
        
        [bFq fqData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(image1b));
        
        bFq = fqData(:,dataColumn);
        
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
        
        fQ = [PatchSeriesProcessor.Fundamental];
        
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
            zs = max(fqData(zi,5));
            %zs2 = max(fqData(zi,6));
            
            tVal = regexprep(num2str(zv,'%3.2e'),'([\d\.]+)(e[+-])[0]?(\d+)','$1$2$3');
            tStd = num2str(zs, '%1.2f');
            %tStd = [num2str(zs, '%1.2f') ' ' num2str(zs2, '%1.2f')];
            
            text(mean(xv2), min(yv)-15, 0, [tVal ' (' tStd ')'], 'Parent', hAxis, 'HorizontalAlignment', 'center', 'VerticalAlignment', 'bottom', 'Color', 'g', 'FontSize', 8, 'FontWeight', 'bold');
            
          end
        end
        
        DBG.dispdbg('Exporting Image...');
        
        img = export_fig(hFig, '-native', '-a2', ['-' renderer]);
        
        delete(hAxis);
        delete(hFig);
      else
        img = image1;
      end
      
    catch err
      debugStamp(err, 1);
      rethrow(err);
    end
    DBG.toc(R);
  end
  
  fxBusy=false;
end


