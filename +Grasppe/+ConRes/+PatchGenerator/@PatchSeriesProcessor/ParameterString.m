function str = GetParameterString(parameters)
  
  parameterGroups = fieldnames(parameters);
  
  str = cell(numel(parameterGroups),1);
  for m = 1:numel(parameterGroups)
    groupName         = parameterGroups{m};
    groupParameters   = parameters.(groupName);
    groupString       = [groupName '{'];
    try
      groupFields     = fieldnames(groupParameters);
      for n = 1:numel(groupFields)
        try
          fieldName   = groupFields{n};
          fieldValue  = parameters.(groupName).(fieldName);
          groupString = [groupString fieldName ': ' toString(fieldValue) ';\t'];
        end
      end
    end
    groupString       = sprintf([strtrim(groupString) '}\t']);
    str{m,1}          = groupString;
  end
  
  str = strcat(str{:}, '\t');
end
