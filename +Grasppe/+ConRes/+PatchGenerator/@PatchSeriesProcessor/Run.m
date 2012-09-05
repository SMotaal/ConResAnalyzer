function output = Run(obj)
  
  import(eval(NS.CLASS));
  %debugging = true;
  
  obj.ResetProcess();
  obj.prepareTasks('Patch Generation', 4);
  
  drawnow expose update;
  
  updateFields        = {'Series', 'Fields', 'Grids'};
  overwriteFields     = {'Processors', 'Parameters', updateFields{:}};
  output              = struct;
  %obj.LoadOutput;
  
  try
    processors  = obj.GetOutputField('Processors', overwriteFields, output);
    if isempty(processors)
      
      %% Prepare Processors
      
      processors.Patch    = obj.PatchProcessor;
      processors.Screen   = obj.ScreenProcessor;
      processors.Scan     = obj.ScanProcessor;
    end
    
    parameters  = obj.GetOutputField('Parameters', overwriteFields, output);
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
    fields          = obj.GetOutputField('Fields');
    if isempty(fields), fields  = obj.ProcessFields(parameters);  end
    
    output.Fields   = fields;
    
    grids           = obj.GetOutputField('Grids');
    if isempty(grids),  grids   = obj.GenerateGrids(fields);      end
    
    output.Grids    = grids;
  catch err
    debugStamp(err, 1);
  end
  
  % obj.SaveOutput;
  
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
  %       try
  %series  = obj.GetOutputField('Series');
  %if isempty(series), series = obj.GenerateSeriesImages([], [], [], [], obj.Tasks.Render); end
  
  series = obj.GenerateSeriesImages([], [], [], [], obj.Tasks.Render);
  
  output.Series = series;
  %series  = GeneratePatches(grids, fields, processors, parameters, obj.Tasks.Render);
  %       catch err
  %         debugStamp(err, 1);
  %         rethrow(err);
  %       end
  
  T = tic;
  seriesStr             = output.Series.Table;
  seriesStr(:, 3:end)   = strrep(lower(seriesStr(:,3:end)), '-', '  ');
  output.Series.Report  = mat2clip(seriesStr);
  toc(T);
  
  obj.SaveOutput;
  
  %output = seriesStr; %[]; %{output{:}, strvcat(seriesStr)};
  
end
