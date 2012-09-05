function fields   = ProcessFields(obj, parameters)
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
  
  selectFields            = @(varargin) obj.FilterFields(fieldTable,varargin{:});
  filterFields            = @(fields)   setdiff(fields, 1:size(fieldCount));
  
  screenFields            = selectFields('Mean', 'Size', 'Screen', 'Scan');
  contoneFields           = selectFields('Patch', 'Addressability', 'Scan');
  monotoneFields          = selectFields('Resolution', 'Size', 'Addressability', 'Scan');
  
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
