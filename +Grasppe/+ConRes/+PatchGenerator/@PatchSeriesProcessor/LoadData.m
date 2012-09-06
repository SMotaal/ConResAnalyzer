function output = LoadData(field, filename)
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
  
  output        = [];
  
  try
    if exist('field', 'var')
      output    = load(pth, field);
      if nargout==1 && numel(fieldnames(output))==1
        output  = output.(field);
        return;
      end
    else
      field     = [];
    end
  end
  
  if isempty(output)
    output      = load(pth); %, '-regexp', '^(O|o)utput$');
  end
  
  %       F = fieldnames(S);
  %       output = S.(F{1});
  
  try if nargout==0 && isempty(field) && defaultPath
      assignin('caller', 'output', output); end; end;
end


% function [output pth] = LoadData(field, filename)
%   persistent outputPath
%   
%   import(eval(NS.CLASS)); % PatchSeriesProcessor
%   
%   defaultPath     = false;
%   
%   try
%     if ~exist('filename', 'var')
%       defaultPath = true;
%       filename    = 'Series';
%       pth         = outputPath;
%     else
%       pth         = '';
%     end
%   end
%   
%   if isempty(pth)
%     pth   = PatchSeriesProcessor.GetResourcePath('output', filename);
%     if defaultPath, outputPath = pth; end
%   end
%   
%   if ~exist('field', 'var') || isempty(field) && defaultPath
%     outputDir     = PatchSeriesProcessor.GetResourcePath('output');
%     files         = dir(fullfile(outputDir, '*.mat'));
%     files         = {files(~[files.isdir]).name}; % Works!
%     matFiles      = files(cellfun(@(x)isequal(x,1), regexpi(files,'^\w+\.mat')));
%     
%     outputFilter  = ~cellfun(@isempty, strfind(matFiles, 'Output'))
%     outputFile    = fullfile(outputDir, matFiles(find(outputFilter, 1)));
%     
%     output        = load(outputFile);
%     
%     for m = find(~outputFilter)
%       matFile     = matFiles(m);
%       
%     end
%     
%   end
%   
%   output        = [];
%   
%   try
%     if exist('field', 'var') && ~isempty(field)
%       if exist([pth field '.mat'], 'file')>0
%         output  = load([pth field '.mat'], field);
%       elseif exist([pth 'Output.mat'], 'file')>0
%         output  = load([pth 'Output.mat'], field);
%       end
%       if nargout==1 && numel(fieldnames(output))==1
%         output  = output.(field);
%         return;
%       end
%     else
%       field     = [];
%     end
%   end
%   
%   if isempty(output)
%     output      = load(pth); %, '-regexp', '^(O|o)utput$');
%   end
%   
%   %       F = fieldnames(S);
%   %       output = S.(F{1});
%   
%   try if nargout==0 && isempty(field) && defaultPath
%       assignin('caller', 'output', output); end; end;
% end

