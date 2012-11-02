function series = GenerateSeriesImages(grids, fields, processors, parameters, task)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; %regexprep(eval(NS.CLASS), '\.\w+$', '.*'));
  import Grasppe.ConRes.Math;
  
  global forceGenerateImages;
  
  forceGenerateImages = isequal(forceGenerateImages, true); %false;
  imageTypes          = {'halftone', 'screen', 'contone', 'monotone'};
  halftoneOutput      = true;
  retinaOutput        = true;
  
  try
    OUTPUT            = evalin('caller', 'output');
    setOuput          = true;
    
    try if ~exist('grids', 'var')   || isempty(grids)
        grids         = OUTPUT.Grids; end;
    end
    
    try if ~exist('fields', 'var')  || isempty(fields)
        fields        = OUTPUT.Fields; end;
    end
    
    try if ~exist('parameters', 'var')  || isempty(parameters)
        parameters    = OUTPUT.Parameters; end;
    end
    
    try if ~exist('processors', 'var')  || isempty(processors)
        processors    = OUTPUT.Processors; end;
    end
    
  catch err
    setOuput          = false;
  end
  
  setTask             = exist('task', 'var') && isa(task, 'Grasppe.Occam.ProcessTask');
  
  fieldTable          = fields.Table;
  seriesRows          = grids.Halftone.Rows;
  seriesRange         = 1:seriesRows;
  
  seriesTable         = cell(seriesRows, 6);
  
  halftoneIDs         = cell(seriesRows, 1);
  screenIDs           = halftoneIDs;
  contoneIDs          = halftoneIDs;
  monotoneIDs         = halftoneIDs;
  
  halftonePaths       = cell(seriesRows, 2);
  screenPaths         = halftonePaths;
  contonePaths        = halftonePaths;
  monotonePaths       = halftonePaths;
  
  screenIdxs          = grids.Screen.Index;
  contoneIdxs         = grids.Contone.Index;
  monotoneIdxs        = grids.Monotone.Index;
  
  screenRefs          = grids.Screen.Reference;
  contoneRefs         = grids.Contone.Reference;
  monotoneRefs        = grids.Monotone.Reference;
  
  %% Prepare Parameters Struct
  fieldNames                    = unique(fieldTable(:,1), 'stable');
  seriesStruct                  = cell(numel(fieldNames)*2, 1);
  seriesStruct(1:2:end)         = fieldNames;
  seriesParameters(seriesRange) = struct(seriesStruct{:});
  seriesVariables(seriesRange)  = struct('Metrics', [], 'Process', []);

  %% Determine loop and display steps
  dSteps              = min(50, max(round(numel(seriesRange)/50)*5, 10));
  mStepper            = Grasppe.Kit.Stepper();
  
  for m = seriesRange % for m = seriesRange
    
    mStep             = mStepper.step(); %s;
    
    if mod(mStep,dSteps)==0
      dispf('Generating Series Images... %d of %d', mStep, seriesRows);
    end
    
    patchProcessor            = [];
    screenProcessor           = [];
    scanProcessor             = [];
    output                    = [];
    referenceImage            = [];
    metrics                   = [];
    
    %% Outputs
    screenOutput              = any(m==screenIdxs);
    contoneOutput             = any(m==contoneIdxs);
    monotoneOutput            = any(m==monotoneIdxs);
    
    %% References
    screenRef                 = screenRefs(m);
    contoneRef                = contoneRefs(m);
    monotoneRef               = monotoneRefs(m);
    
    %% Parameters
    parameters                = struct;
    valueIndex                = grids.Halftone.Grid(m, :);
    
    for n = 1:size(fieldTable, 1)
      v                       = valueIndex(n);
      if v>0, v               = fieldTable{n,3}(v); end
      parameters.(fieldTable{n,1}).(fieldTable{n,2}) = v;
    end
    
    %parameters.Screen.PixelResolution = 3600;
    
    seriesParameters(m)       = parameters;
    
    %% Variables & Calculations
    variables                 = seriesVariables(m);
    try metrics               = variables.Metrics; end
    
    patchResolution           = parameters.Patch.Resolution;
    patchSize                 = parameters.Patch.Size;
    imageResolution           = parameters.Scan.Resolution*parameters.Scan.Scale/100;
    
    pixelAcuity               = Math.VisualResolution(imageResolution) * 7;
    fundamentalFrequency      = Math.FundamentalFrequency(patchResolution, patchSize , imageResolution);
    [B W]                     = Math.FrequencyRange(patchSize, imageResolution);

    metrics.BandParameters    = [B W];
    metrics.PixelAcuity       = pixelAcuity;
    metrics.Fundamental       = fundamentalFrequency;
    
    variables.Metrics         = metrics;
    
    %     pixelResolution           = parameters.Scan.Resolution*parameters.Scan.Scale/100/25.4;
    %     viewingDistance           = 30*10; % mm
    %     retinalAcuity             = 1/60 * pi/180;
    %     retinalResolution         = viewingDistance*(tan(retinalAcuity/2));
    %     pixelAcuity               = pixelResolution*retinalResolution*7;
    
    %% Detemine IDs
    halftoneID                = PatchSeriesProcessor.GetParameterID(parameters);
    screenID                  = [];
    contoneID                 = [];
    monotoneID                = [];
    
    if screenOutput
      screenID                = PatchSeriesProcessor.GetParameterID(parameters, 'screen');    end
    if contoneOutput
      contoneID               = PatchSeriesProcessor.GetParameterID(parameters, 'contone');   end
    if monotoneOutput
      monotoneID              = PatchSeriesProcessor.GetParameterID(parameters, 'monotone');  end
    
    
    %% Load Images
    generateImages            = true;
    
    try
      halftoneImage           = PatchSeriesProcessor.LoadImage('halftone', halftoneID);
      
      if ~isempty(halftoneImage)
        if ~forceGenerateImages && screenOutput
          screenImage           = PatchSeriesProcessor.LoadImage('screen', screenID);
          screenRetina          = PatchSeriesProcessor.LoadImage('screen retinaImage', screenID);
        end

        if ~forceGenerateImages && contoneOutput
          contoneImage          = PatchSeriesProcessor.LoadImage('contone', contoneID);
          contoneRetina         = PatchSeriesProcessor.LoadImage('contone retinaImage', contoneID);
        end
        if ~forceGenerateImages && monotoneOutput
          monotoneImage         = PatchSeriesProcessor.LoadImage('monotone', monotoneID);
          monotoneRetina        = PatchSeriesProcessor.LoadImage('monotone retinaImage', monotoneID);
        end

        if ~forceGenerateImages
          generateImages        = false;
        end
      end
      
    catch err
      debugStamp(err,5);
    end
    
    %% Processors
    if generateImages
      patchProcessor          = Grasppe.ConRes.PatchGenerator.Processors.Patch;
      screenProcessor         = Grasppe.ConRes.PatchGenerator.Processors.Screen;
      scanProcessor           = Grasppe.ConRes.PatchGenerator.Processors.Scan;
      
      %% Image Models
      output                  = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      
      output.Variables.PixelAcuity  = pixelAcuity;
      output.Variables.Fundamental  = fundamentalFrequency;
      
      %% Generate Patch Image
      patchProcessor.Execute(parameters.Patch, output);
      patchImage              = output.Image;
      
      %% Generate Halftone Image
      screenProcessor.Execute(parameters.Screen, output);
      screenImage             = screenProcessor.HalftoneImage.Image;
      
      scanProcessor.Execute(parameters.Scan, output);
      scanImage               = output.Image;
      
      if screenOutput
        screenImage           = imresize(screenImage,  size(scanImage));
      end
      
      if contoneOutput || monotoneOutput
        referenceImage        = imresize(patchImage,  size(scanImage));
      end
      
    else
      scanImage       = [];
      screenImage     = [];
      referenceImage  = [];
    end
    
    typeID                    = '';
    outFlags                  = {pixelAcuity, generateImages};
    
    %% Output Halftone, Screen, Contone, Monotone (or use reference)
    if halftoneOutput
      typeID                  = [typeID 'H'];
      halftoneIDs{m}          = halftoneID;
      [pths imgs]             = processImages(scanImage, 'halftone', halftoneID, outFlags{:});
      halftonePaths(m,:)      = pths(:);
    end
    if screenOutput
      typeID                  = [typeID 'S'];
      screenIDs{m}            = screenID;
      [pths imgs]             = processImages(screenImage, 'screen', screenID, outFlags{:});
      screenPaths(m,:)        = pths(:);
    end
    if contoneOutput
      typeID                  = [typeID 'C'];
      contoneIDs{m}           = contoneID;
      [pths imgs]             = processImages(referenceImage, 'contone', contoneID, outFlags{:});
      contonePaths(m,:)       = pths(:);
    end
    if monotoneOutput
      typeID                  = [typeID 'M'];
      monotoneIDs{m}          = monotoneID;
      [pths imgs]             = processImages(referenceImage, 'monotone', monotoneID, outFlags{:});
      monotonePaths(m,:)      = pths(:);
    end
        
    
    try variables.Process     = output.Variables; end
    seriesVariables(m)        = variables;
    
    seriesTable(m,:)          = {numel(typeID), typeID, [], halftoneID, [], []};
    
    if generateImages
      try delete(patchProcessor);   end
      try delete(screenProcessor);  end
      try delete(scanProcessor);    end
      try delete(output);           end
      try delete(screenImage);      end
    end    
  end
  
  try delete(mStepper); end
  
  %% References
  screenIDs                   = screenIDs(screenIdxs);
  contoneIDs                  = contoneIDs(contoneIdxs);
  monotoneIDs                 = monotoneIDs(monotoneIdxs);
  
  screenPaths                 = screenPaths(screenIdxs, :);
  contonePaths                = contonePaths(contoneIdxs, :);
  monotonePaths               = monotonePaths(monotoneIdxs, :);
  
  seriesTable(:, 3)           = screenIDs(screenRefs);
  seriesTable(:, 4)           = halftoneIDs;
  seriesTable(:, 5)           = contoneIDs(contoneRefs);
  seriesTable(:, 6)           = monotoneIDs(monotoneRefs);
  
  series.Table                = seriesTable;
  series.Parameters           = seriesParameters;
  series.Variables            = seriesVariables;
  series.Paths.Halftone       = halftonePaths;
  series.Paths.Screen         = screenPaths;
  series.Paths.Contone        = contonePaths;
  series.Paths.Monotone       = monotonePaths;
  
  if setOuput || nargout==0
    OUTPUT.Series              = series;
    assignin('caller', 'output', OUTPUT);
  end
end

% function m = mStep(m)
%   
%   persistent M;
%   
%   if nargin>0, M = m;
%   else
%     if isempty(M), M = 0; end
%     M = M +1;
%   end
%   
%   m = M;
%   
% end

function [pths imgs] = processImages(src, type, id, retinalAccuity, imageOut)
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor

  try
    pths                    = cell(1, 2);
    imgs                    = cell(1, 2);
    
    retinaOut = exist('retinalAccuity', 'var') & isscalar(retinalAccuity) & isnumeric(retinalAccuity);
    imageOut  = ~exist('imageOut',  'var') | isequal(imageOut , true);
    
    if ~imageOut
      pths(1)               = {PatchSeriesProcessor.GetResourcePath([type ' image'], id, 'png')};
      if retinaOut, pths(2) = {PatchSeriesProcessor.GetResourcePath([type ' retinaImage'], id, 'png')}; end
      return;
    end
    
    if isnumeric(src)
      imgs(1)               = {src};
    elseif isobject(src)
      imgs(1)               = {src.Image};
    end
    
    pths(1)                 = {PatchSeriesProcessor.SaveImage(imgs{1}, type, id)};
    
    if ~retinaOut, return; end
    
    disk                    = @(x, y) imfilter(x,fspecial('disk',y),'replicate');
    
    imgs(2)                 = {disk(imgs{1}, retinalAccuity)};
    pths(2)                 = {PatchSeriesProcessor.SaveImage(imgs{2}, [type ' retinaImage'], id)};
  catch err
    debugStamp(err, 1);
    return;
  end
end
