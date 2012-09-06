function fields   = ProcessSeriesFields(parameters)
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  output        = struct;
  setOuput      = false;
  try
    output      = evalin('caller', 'output');
    setOuput    = true;
  end
  
  %% Determine Variable Fields
  
  fieldTable  = cell(0,3);
  fieldVars   = [];
  
  parameterGroups = fieldnames(parameters);
  try
    for m = 1:numel(parameterGroups)-1
      groupName       = parameterGroups{m};
      groupParameters = parameters.(groupName);
      try
        groupFields     = fieldnames(groupParameters);
        for n = 1:numel(groupFields)
          try
            fieldName                   = groupFields{n};
            fieldValue                  = parameters.(groupName).(fieldName);
            fieldTable(end+1, 1:3)  = {groupName, fieldName, fieldValue};
            if ~ischar(fieldValue) && numel(fieldValue)>1
              fieldVars(end+1)       = numel(fieldValue);
            else
              fieldVars(end+1)       = 1;
            end
          catch err
            debugStamp(err, 2);
          end
        end
      catch err
        debugStamp(err, 2);
      end
    end
    
    fieldCount  = numel(fieldVars);
  catch err
    debugStamp(err, 1);
  end
  
  % fieldRange        = cell(fieldCount,1);
  
  %% Geenrate Field Filters
  
  %selectFields            = @(varargin) filterFields(fieldTable,varargin{:});
  filterFields            = @(fields)   setdiff(fields, 1:size(fieldCount));
  
  screenFields            = selectFields(fieldTable, 'Mean', 'Size', 'Screen', 'Scan');
  contoneFields           = selectFields(fieldTable, 'Patch', 'Addressability', 'Scan');
  monotoneFields          = selectFields(fieldTable, 'Resolution', 'Size', 'Addressability', 'Scan');
  
  screenFilter            = filterFields(screenFields);
  contoneFilter           = filterFields(contoneFields);
  monotoneFilter          = filterFields(monotoneFields);
  
  %% Compose Fields Structure
  
  fields.Count            = fieldCount;
  fields.Variables        = fieldVars;
  fields.Table            = fieldTable;
  fields.Groups           = parameterGroups;
  
  fields.Fields.Screen    = screenFields;
  fields.Fields.Contone   = contoneFields;
  fields.Fields.Monotone  = monotoneFields;
  
  fields.Filters.Screen   = screenFilter;
  fields.Filters.Contone  = contoneFilter;
  fields.Filters.Monotone = monotoneFilter;
  
  output.Fields           = fields;
  
  if setOuput, assignin('caller', 'output', output); end
end



function fieldRange = selectFields(table, varargin)
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  if isstruct(table) && isfield(table, 'Table')
    table = fields.Table;
  end
  
  if ~iscellstr(varargin), error('Grasppe:FilterFields:InvalidNames', ...
      'Field filters must be specified as cellstr.'); end
  
  fieldRange = [];
  
  for m = 1:numel(varargin)
    try
      fieldRows     = [];
      
      fieldNames    = regexp(varargin{m}, '\w+', 'match');
      
      if numel(fieldNames)== 2
        
        groupRows   = strcmpi(fieldNames{1}, table(:,1));
        fieldRows   = strcmpi(fieldNames{2}, table(:,2));
        
        fieldRows   = find(groupRows+fieldRows==2);
        
      elseif numel(fieldNames)==1
        
        fieldRows   = find(strcmpi(fieldNames{1}, table(:,1)));
        
        if isempty(fieldRows),
          fieldRows = find(strcmpi(fieldNames{1}, table(:,2)), 1, 'first');
        end
        
      end
      
      fieldRange  = [fieldRange(:); fieldRows(:)]';
      
    catch err
      debugStamp(err, 1);
      continue;
    end
    
  end
  
end

