function grids = GenerateSeriesGrids(fields)
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  output        = struct;
  setOuput      = false;
  try
    output      = evalin('caller', 'output');
    setOuput    = true;
  end
  
  %% Generate Variable Grid
  
  fieldCount    = fields.Count;
  % fieldRange  = fields.Range;
  fieldVars     = fields.Variables;
  fieldTable    = fields.Table;
  fieldFilters  = fields.Filters;
  
  
  varRange    = '';
  for m = 1:fieldCount
    fieldRange{m} = 1:fieldVars(m);
    varRange  = [varRange 'varGrid{' int2str(m) '} '];
  end
  
  eval(['[' varRange '] = ndgrid(fieldRange{:});']);
  
  varCount      = numel(varGrid{1});
  
  %% Compile Patch-Variable Index Grid
  
  patchGrid   = zeros(fieldCount, varCount);
  
  for m = 1:fieldCount
    patchGrid(m,:) = varGrid{m}(:);
  end
  patchGrid = patchGrid';
  
  try
    
    % Determine Valid Halftone Patch Grid
    meanRange                   = fieldTable{1,3}(patchGrid(:,1));
    conRange                    = fieldTable{2,3}(patchGrid(:,2));
    resRange                    = fieldTable{3,3}(patchGrid(:,3));
    halftoneGrid                = patchGrid((meanRange+conRange./2)<=100 & (meanRange-conRange./2)>=0, :);
    halftoneRows                = size(halftoneGrid,1);
    
    % Determine Screening Patch Index
    screenFilter                = fields.Fields.Screen;
    [g screenIdx screenRef]     = unique(halftoneGrid(:, screenFilter), 'rows', 'stable');
    screenGrid                  = halftoneGrid(screenIdx, :);
    screenRows                  = size(screenGrid,1);
    
    % Determine Contone Patch Index
    contoneFilter               = fields.Fields.Contone;
    [g contoneIdx contoneRef]   = unique(halftoneGrid(:, contoneFilter), 'rows', 'stable');
    contoneGrid                 = halftoneGrid(contoneIdx, :);
    contoneRows                 = size(contoneGrid,1);
    
    % Determine Monotone Patch Index
    monotoneFilter              = fields.Fields.Monotone;
    [g monotoneIdx monotoneRef] = unique(halftoneGrid(:, monotoneFilter), 'rows', 'stable');
    monotoneGrid                = halftoneGrid(monotoneIdx, :);
    monotoneRows                = size(monotoneGrid,1);
    
    
    % Validate Series Indices
    validateIdx                 = @(r, g) all(r<=halftoneRows) & all(all(halftoneGrid(r, :)-g==0));
    screenValid                 = validateIdx(screenIdx,    screenGrid  );
    contoneValid                = validateIdx(contoneIdx,   contoneGrid );
    monotoneValid               = validateIdx(monotoneIdx,  monotoneGrid);
    seriesValid                 = screenValid & contoneValid & monotoneValid;
    
  catch err
    debugStamp(err, 1);
  end
  
  
  grids.Range.Fields        = fieldRange;
  grids.Range.Grid          = patchGrid;
  grids.Range.Rows          = varCount;
  grids.Range.Columns       = fieldCount;
  grids.Range.MeanTone      = meanRange;
  grids.Range.Contrast      = conRange;
  grids.Range.Resolution    = resRange;
  
  grids.Halftone.Grid       = halftoneGrid;
  grids.Halftone.Rows       = halftoneRows;
  grids.Halftone.Columns    = fieldCount;
  
  grids.Screen.Grid         = screenGrid;
  grids.Screen.Index        = screenIdx;
  grids.Screen.Reference    = screenRef;
  grids.Screen.Rows         = screenRows;
  grids.Screen.Columns      = fieldCount;
  
  grids.Contone.Grid        = contoneGrid;
  grids.Contone.Index       = contoneIdx;
  grids.Contone.Reference   = contoneRef;
  grids.Contone.Rows        = contoneRows;
  grids.Contone.Columns     = fieldCount;
  
  grids.Monotone.Grid       = monotoneGrid;
  grids.Monotone.Index      = monotoneIdx;
  grids.Monotone.Reference  = monotoneRef;
  grids.Monotone.Rows       = monotoneRows;
  grids.Monotone.Columns    = fieldCount;
  
  output.Grids              = grids;
  if setOuput, assignin('caller', 'output', output); end
end
