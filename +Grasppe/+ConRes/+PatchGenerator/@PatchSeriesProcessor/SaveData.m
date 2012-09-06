function SaveData(output, filename)
  persistent outputPath
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  defaultPath     = false;
  
  try
    if ~exist('filename', 'var')
      defaultPath = true;
      filename    = 'SeriesData';
      pth         = outputPath;
    else
      pth         = '';
    end
  end
  
  if isempty(pth)
    pth  = PatchSeriesProcessor.GetResourcePath('output', filename, 'mat');
    
    if defaultPath, outputPath = pth; end
  end
  
  try
    
    try if ~exist('output', 'var') && defaultPath
        output      = evalin('caller', 'output'); end; end;
    
    save(pth, '-struct', 'output');
  catch err
    debugStamp(err, 1);
    return;
  end
end
