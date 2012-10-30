function output = Run(obj)
  
  import(eval(NS.CLASS));   % PatchSeriesProcessor
  %debugging = true;
  
  obj.ResetProcess();
  obj.prepareTasks('Patch Generation', 4);
  
  drawnow expose update;
  
  updateFields        = {'Series'}; % 'Series', 'Fields', 'Grids'};
  overwriteFields     = {'Processors', 'Parameters', updateFields{:}};
  output              = struct;
  
  try
    processors  = PatchSeriesProcessor.GetFieldData('Processors', overwriteFields, output);
    if isempty(processors)
      
      %% Prepare Processors
      
      processors.Patch    = obj.PatchProcessor;
      processors.Screen   = obj.ScreenProcessor;
      processors.Scan     = obj.ScanProcessor;
    end
    
    parameters  = PatchSeriesProcessor.GetFieldData('Parameters', overwriteFields, output);
    if isempty(parameters)
      parameters          = obj.PrepareParameters;
    end
    %determine parameters
  catch err
    debugStamp(err, 1);
  end
  
  output.Parameters   = parameters;
  output.Processors   = processors;
  
  
  CHECK(obj.Tasks.Prep);
  % PREP 1/4 ***************************************************************
  
  try
    fields          = PatchSeriesProcessor.GetFieldData('Fields');
    if isempty(fields), fields  = PatchSeriesProcessor.ProcessSeriesFields(parameters);  end
    
    output.Fields   = fields;
    
    grids           = PatchSeriesProcessor.GetFieldData('Grids');
    if isempty(grids),  grids   = PatchSeriesProcessor.GenerateSeriesGrids(fields);      end
    
    output.Grids    = grids;
  catch err
    debugStamp(err, 1);
  end
  
  PatchSeriesProcessor.SaveData();
  
  CHECK(obj.Tasks.Prep);
  % PREP 2/4 ***************************************************************
  
  try
    %% Prepare Processors
    
  catch err
    debugStamp(err, 1);
  end
  
  % PREP 3/4 ***************************************************************
  CHECK(obj.Tasks.Prep);
  %
  %       %% Variable Tasks Load Estimation
  %       nPatches    = [halftoneRows];
  %       nExtras     = [halftoneRows, screenRows, contoneRows, monotoneRows];
  %       nImages     = 2;
  %       nStatistics = 1;
  %       nSteps      = [nImages nStatistics]; % [Image+Retina Statistics
  %       n         = numel(fieldnames(obj.Parameters.Processors));
  %       n         = n + 2*(n-5) + 2;
  %
  SEAL(obj.Tasks.Prep); % 3
  
  % obj.updateTasks('Generating Patch', n);
  try
    
    series  = PatchSeriesProcessor.GetFieldData('Series');
    
    if isempty(series)
      series = PatchSeriesProcessor.GenerateSeriesImages(grids, fields, processors, parameters, obj.Tasks.Render);
      output.Series = series;
      PatchSeriesProcessor.SaveData();
    end
    
    series = PatchSeriesProcessor.GenerateSeriesFFT(series, grids, fields, processors, parameters, obj.Tasks.Render);
    
    PatchSeriesProcessor.SaveData();
    
    PatchSeriesProcessor.GenerateSeriesStatistics(series, grids, fields, processors, parameters, obj.Tasks.Render);
    
    output.Series = series;
    
    seriesStr             = output.Series.Table;
    seriesStr(:, 3:end)   = strrep(lower(seriesStr(:,3:end)), '-', '  ');
    output.Series.Report  = mat2clip(seriesStr);
    
    PatchSeriesProcessor.SaveData();
    
  catch err
    disp(err.message);
    debugStamp(err, 1);
    beep;
  end
  
  
end
