function fieldOutput = GetFieldData(fieldName, overwriteFields, output)
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  try if ~exist('overwriteFields', 'var')
      overwriteFields = evalin('caller', 'overwriteFields'); end;
  end;
  try if ~exist('output', 'var')
      output          = evalin('caller', 'output'); end;
  catch err
    output            = struct;
  end;
  
  fieldOutput         = [];
  try
    if isfield(output, fieldName)
      fieldOutput     = output.(fieldName);
    elseif ~any(strcmpi(fieldName, overwriteFields));
      fieldOutput     = PatchSeriesProcessor.LoadData(fieldName);
    end
  end
end
