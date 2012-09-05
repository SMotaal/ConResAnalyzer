function fieldRange = FilterFields(obj, table, varargin)
  
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
