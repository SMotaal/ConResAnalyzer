function fieldOutput = GetOutputField(obj, fieldName, overwriteFields, output)
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
    if isfield(output, fieldName) || any(strcmpi(fieldName, overwriteFields));
      fieldOutput     = output.(fieldName);
    else
      fieldOutput     = obj.LoadOutput(fieldName);
    end
  end
end
