function SaveOutput(obj, output)
  persistent outputPath
  
  try if ~exist('output', 'var')
      output      = evalin('caller', 'output'); end; end;
  
  if isempty(outputPath)
    outputPath  = obj.GetPath('output', 'series', 'mat');
  end
  
  save(outputPath, '-struct', 'output');
end
