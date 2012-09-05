function series = GenerateSeriesImages(obj, grids, fields, processors, parameters, task)
  
  forceGenerate       = false;
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
  
  parfor m = 1:seriesRows
    
    patchProcessor            = [];
    screenProcessor           = [];
    scanProcessor             = [];
    output                    = [];
    referenceImage            = [];
    
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
    
    parameters.Screen.PixelResolution = obj.PatchProcessor.Addressability;
    
    seriesParameters(m)       = parameters;
    
    %% Retina Filter Calculation
    pixelResolution           = parameters.Scan.Resolution*parameters.Scan.Scale/100/25.4;
    viewingDistance           = 30*10; % mm
    retinalAccuity            = 1/60 * pi/180;
    retinalResolution         = viewingDistance*(tan(retinalAccuity/2));
    pixelAccuity              = pixelResolution*retinalResolution*7;
    
    %% Detemine IDs
    halftoneID                = obj.ParameterID(parameters);
    screenID                  = [];
    contoneID                 = [];
    monotoneID                = [];
    if screenOutput
      screenID                = obj.ParameterID(parameters, 'screen');    end
    if contoneOutput
      contoneID               = obj.ParameterID(parameters, 'contone');   end
    if monotoneOutput
      monotoneID              = obj.ParameterID(parameters, 'monotone');  end
    
    
    %% Load Images
    generateImages            = true;
    
    try
      halftoneImage           = obj.LoadImage('halftone', halftoneID);
      
      if screenOutput || ~forceGenerate
        screenImage           = obj.LoadImage('screen', screenID);
        screenRetina          = obj.LoadImage('screen retinaImage', screenID);
      end
      
      if contoneOutput || ~forceGenerate
        contoneImage          = obj.LoadImage('contone', contoneID);
        contoneRetina         = obj.LoadImage('contone retinaImage', contoneID);
      end
      if monotoneOutput || ~forceGenerate
        monotoneImage         = obj.LoadImage('monotone', monotoneID);
        monotoneRetina        = obj.LoadImage('monotone retinaImage', monotoneID);
      end
      
      generateImages          = false;
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
      
      if retinaOutput
        output.Variables.PixelAccuity   = pixelAccuity; % Pixels per mm for theta 1/60
      end
      
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
    outFlags                  = {pixelAccuity, generateImages};
    
    %% Output Halftone, Screen, Contone, Monotone (or use reference)
    if halftoneOutput
      typeID                  = [typeID 'H'];
      halftoneIDs{m}          = halftoneID;
      [pths imgs]             = obj.OutputImages(scanImage, 'halftone', halftoneID, outFlags{:});
      halftonePaths(m,:)      = pths;
    end
    if screenOutput
      typeID                  = [typeID 'S'];
      screenIDs{m}            = screenID;
      [pths imgs]             = obj.OutputImages(screenImage, 'screen', screenID, outFlags{:});
      screenPaths(m,:)        = pths;
    end
    if contoneOutput
      typeID                  = [typeID 'C'];
      contoneIDs{m}           = contoneID;
      [pths imgs]             = obj.OutputImages(referenceImage, 'contone', contoneID, outFlags{:});
      contonePaths(m,:)       = pths;
    end
    if monotoneOutput
      typeID                  = [typeID 'M'];
      monotoneIDs{m}          = monotoneID;
      [pths imgs]             = obj.OutputImages(referenceImage, 'monotone', monotoneID, outFlags{:});
      monotonePaths(m,:)      = pths;
    end
    
    if generateImages
      try delete(patchProcessor);   end
      try delete(screenProcessor);  end
      try delete(scanProcessor);    end
      try delete(output);           end
      try delete(screenImage);      end
    end
    
    seriesTable(m,:)          = {numel(typeID), typeID, [], halftoneID, [], []};
  end
  
  %% References
  screenIDs                   = screenIDs(screenIdxs);
  contoneIDs                  = contoneIDs(contoneIdxs);
  monotoneIDs                 = monotoneIDs(monotoneIdxs);
  
  screenPaths                 = screenPaths(screenIdxs);
  contonePaths                = contonePaths(contoneIdxs);
  monotonePaths               = monotonePaths(monotoneIdxs);
  
  seriesTable(:, 3)           = screenIDs(screenRefs);
  seriesTable(:, 4)           = halftoneIDs;
  seriesTable(:, 5)           = contoneIDs(contoneRefs);
  seriesTable(:, 6)           = monotoneIDs(monotoneRefs);
  
  series.Table                = seriesTable;
  series.Parameters           = seriesParameters;
  series.Paths.Halftone       = halftonePaths;
  series.Paths.Screen         = screenPaths;
  series.Paths.Contone        = contonePaths;
  series.Paths.Monotone       = monotonePaths;
  
  if setOuput || nargout==0
    OUPUT.Series              = series;
    assignin('caller', 'output', OUPUT);
  end
end
