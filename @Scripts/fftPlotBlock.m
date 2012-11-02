function [ stackImage ] = fftPlotBlock(varargin)
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  import Grasppe.ConRes.Math;
  import Grasppe.ConRes.PatchGenerator.Processors.*;
  
  global forceRenderBlocks;
  
  forceRenderBlocks           = isequal(forceRenderBlocks, true); %false;
  
  LPI                         = Screen.LPI;
  SPI                         = Screen.SPI;

  data                        = loadSeriesData(varargin{:});
  
  fieldTable                  = data.Fields.Table;  
  seriesParameters            = data.Series.Parameters;
  seriesTable                 = data.Series.Table;
  seriesParameters            = data.Series.Parameters;
  seriesVariables             = data.Series.Variables;
  
  seriesRows                  = data.Grids.Halftone.Rows;
  seriesRange                 = 1:seriesRows;
   
  meanToneRange               = data.Parameters.Patch.Mean;
  contrastRange               = data.Parameters.Patch.Contrast; % (1);
  resolutionRange             = data.Parameters.Patch.Resolution;
  parameters                  = struct(data.Series.Parameters(1));
  
  try
    parameters.Screen.(LPI)   = min(data.Parameters.(LPI));
    parameters.Screen.(SPI)   = max(data.Parameters.(SPI));
  end
  
  meanToneSteps               = numel(meanToneRange);

  meanTone                    = 50; %meanToneRange(m);
  [stackImage stackID]        = processStack(meanTone, parameters, contrastRange, resolutionRange, seriesTable, varargin{:});
  
  
end

function [stackImage stackID] = processStack(meanTone, parameters, contrastRange, resolutionRange, seriesTable, varargin)
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
    parameters.Patch.Mean     = meanTone;
    
    stackParameters           = parameters;
    stackParameters.Patch     = rmfield(stackParameters.Patch, {'Contrast', 'Resolution'});
    stackID                   = PatchSeriesProcessor.GetParameterID(stackParameters);
    
    stackGrid                 = generateStackGrid(meanTone, contrastRange, resolutionRange, parameters, seriesTable);
    stackImage                = assembleStackImage(stackGrid, meanTone, varargin{:});
end

function data = loadSeriesData(varargin)
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  if numel(varargin)>0
    series                    = varargin{1};
    if isstruct(series), data = series; return; end
    if ischar(series), PatchSeriesProcessor.SeriesID(series); end
  end
      
  data                        = PatchSeriesProcessor.LoadData();
  %     data.SRF              = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
  %     data.PRF              = PatchSeriesProcessor.LoadData('PRF', 'PRFData');
end

function stackGrid = generateStackGrid(meanTone, contrastRange, resolutionRange, params, seriesTable)
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor;
  
  resolutionSteps             = numel(resolutionRange);
  contrastSteps               = numel(contrastRange);
  
  stackGrid                   = cell(resolutionSteps, contrastSteps);
  
  params                      = struct(params);
  
  for n = 1:contrastSteps
    
    contrast                  = contrastRange(n);
    params.Patch.Contrast     = contrast;
    
    %if ~((meanTone+contrast) <= 100 && (meanTone-contrast) >= 0), continue; end
    
    for m = 1:resolutionSteps
      resolution              = resolutionRange(m);
      params.Patch.Resolution = resolution;
            
      patchID                 = PatchSeriesProcessor.GetParameterID(params);
      
      if ~any(strcmpi(seriesTable(:,4), patchID)), continue; end;
      
      stackGrid{m, n}         = patchID; %patchPath;
    end
  end
end

function stackImage = assembleStackImage(stackGrid, meanTone, varargin)
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor  
  
  stackImages             = cell(size(stackGrid));
  
  patchWidth              = [];
  patchHeight             = [];
  
  stackRows               = size(stackGrid,1);
  stackColumns            = size(stackGrid,2);
  
  imageType               = 'Halftone';
  try imageType           = varargin{2}; end
  
  for m = 1:stackRows
    for n = 1:stackColumns
      patchID             = stackGrid{m,n};
      
      if isempty(patchID), continue; end;
      
      try
        %stackImages{m,n}  = PatchSeriesProcessor.LoadImage('Halftone Image', patchID);
        stackImages{m,n}  = PatchSeriesProcessor.LoadImage([imageType ' Image'], patchID);
        
        patchHeight       = min([patchHeight  size(stackImages{m,n}, 1)]);
        patchWidth        = min([patchWidth   size(stackImages{m,n}, 2)]);
      catch err
      end
    end
  end
  
  bufferWidth             = 0; %round(patchWidth/3);
  
  stackWidth              = (patchWidth+bufferWidth)*stackColumns;
  stackHeight             = patchHeight*stackRows;
    
  stackImage              = ones(stackHeight, stackWidth) .* (100-meanTone)/100;
  
  for m = 1:stackRows
    for n = 1:stackColumns
      patchImage          = im2double(stackImages{m, n});
      if isempty(patchImage), continue; end;
      
      x1                  = 1+((n-1)*(patchWidth+bufferWidth));
      x2                  = x1+patchWidth-1;      
      y1                  = 1+((m-1)*patchHeight);
      y2                  = y1+patchHeight-1;
      
      try
        stackImage(y1:y2, x1:x2)  = patchImage(1:patchHeight, 1:patchWidth);
        
      catch err
        debugStamp;
      end
    end
  end
  
  
  try
    pad                   = 10;
    paddedBlock           = zeros(size(stackImage)+(pad*2));                    % stackHeight+20, stackWidth+20);
    paddedBlock(pad+1:pad+stackHeight, pad+1:pad+stackWidth) = stackImage;
    stackImage            = paddedBlock;
  catch err
    debugStamp;
  end
  
  return;
  
end
