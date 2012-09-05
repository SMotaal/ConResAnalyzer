function output = LoadOutput(obj, field)
  persistent outputPath
  
  if isempty(outputPath)
    outputPath  = obj.GetPath('output', 'series', 'mat');
  end
  
  output        = [];
  
  try
    if exist('field', 'var')
      output    = load(outputPath, field);
      if nargout==1 && numel(fieldnames(output))==1
        output  = output.(field);
        return;
      end
    else
      field     = [];
    end
  end
  
  if isempty(output)
    output      = load(outputPath); %, '-regexp', '^(O|o)utput$');
  end
  
  %       F = fieldnames(S);
  %       output = S.(F{1});
  
  try if nargout==0 && isempty(field)
      assignin('caller', 'output', output); end; end;
end
