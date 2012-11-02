function [ output_args ] = exportBlock(data) % SRF, Series )
  %TALLYSRFDATA Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  import Grasppe.ConRes.Math;
  import Grasppe.ConRes.PatchGenerator.Processors.*;
  
  global forceRenderBlocks;
  
  forceRenderBlocks         = isequal(forceRenderBlocks, true); %false;
  
  %   forceOutput = true;
  
  %   INT                       = '%d';
  %   DEC                       = @(x) ['%1.' int2str(x) 'f'];
  %   SCI                       = '%1.3fe%d';
  %   STR                       = '%s';
  %   TAB                       = '\t';
  
  if ~exist('data', 'var')
    data                    = PatchSeriesProcessor.LoadData();
    %     data.SRF                = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
    %     data.PRF                = PatchSeriesProcessor.LoadData('PRF', 'PRFData');
  end
  
  parameters                = data.Series.Parameters;
  
  fieldTable                = data.Fields.Table;
  seriesRows                = data.Grids.Halftone.Rows;
  seriesRange               = 1:seriesRows;
  
  %   htRows                    = data.Grids.Halftone.Rows;
  %   scRows                    = data.Grids.Screen.Rows;
  %   ctRows                    = data.Grids.Contone.Rows;
  %   mtRows                    = data.Grids.Monotone.Rows;
  %
  %   scIdxs                    = data.Grids.Screen.Index;
  %   ctIdxs                    = data.Grids.Contone.Index;
  %   mtIdxs                    = data.Grids.Monotone.Index;
  %
  %   scRefs                    = data.Grids.Screen.Reference;
  %   ctRefs                    = data.Grids.Contone.Reference;
  %   mtRefs                    = data.Grids.Monotone.Reference;
  
  seriesTable               = data.Series.Table;
  seriesParameters          = data.Series.Parameters;
  seriesVariables           = data.Series.Variables;
  
  htPaths                   = data.Series.Paths.Halftone;
  %scPaths                   = data.Series.Paths.Screen;
  %ctPaths                   = data.Series.Paths.Contone;
  %mtPaths                   = data.Series.Paths.Monotone;
  
  
  meanToneRange             = data.Parameters.Patch.Mean;
  contrastRange             = data.Parameters.Patch.Contrast;
  resolutionRange           = data.Parameters.Patch.Resolution;
  baseParameters            = struct(data.Series.Parameters(1));
  paths                     = htPaths(:,1);
  
  
  try
    baseParameters.Screen.(Screen.LPI)  = max(data.Parameters.(Screen.LPI));
    baseParameters.Screen.(Screen.SPI)  = max(data.Parameters.(Screen.SPI));
  end
  
  meanToneSteps             = numel(meanToneRange);
  
  for m = 1:meanToneSteps
    meanTone                = meanToneRange(m);
    
    params                  = baseParameters;
    params.Patch.Mean       = meanTone;
    
    blockParams             = params;
    blockParams.Patch       = rmfield(blockParams.Patch, {'Contrast', 'Resolution'});
    blockId                 = PatchSeriesProcessor.GetParameterID(blockParams);
    blockGrid               = generateBlockGrid(meanTone, contrastRange, resolutionRange, params, seriesTable);
    
    [htImg ctImg mtImg]     = assembleBlockImage(blockGrid, meanTone);
    
    PatchSeriesProcessor.SaveImage(htImg,   'Halftone BlockComp', blockId);
    PatchSeriesProcessor.SaveImage(ctImg,   'Contone BlockComp',  blockId);
    
    %if isequal(meanTone, 50)
      PatchSeriesProcessor.SaveImage(mtImg,   'Monotone BlockComp', blockId);
    %end
    
  end
  
end

function blockGrid = generateBlockGrid(meanTone, contrastRange, resolutionRange, params, seriesTable)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor;
  
  resolutionSteps             = numel(resolutionRange);
  contrastSteps               = numel(contrastRange);
  
  blockGrid                   = cell(resolutionSteps, contrastSteps);
  
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
      
      blockGrid{m, n}         = patchID; %patchPath;
    end
  end
end

function [htImage ctImage mtImage] = assembleBlockImage(blockGrid, meanTone)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor  
  
  htImages                = cell(size(blockGrid));
  ctImages                = cell(size(blockGrid));
  mtImages                = cell(size(blockGrid));
  %scImages                = cell(size(blockGrid));
  
  patchWidth              = [];
  patchHeight             = [];
  
  blockRows               = size(blockGrid,1);
  blockColumns            = size(blockGrid,2);
  
  for m = 1:blockRows
    for n = 1:blockColumns
      patchID             = blockGrid{m,n};
      
      if isempty(patchID), continue; end;
      
      try
        htImages{m,n}     = PatchSeriesProcessor.LoadImage('Halftone Image', patchID);
        ctImages{m, n}    = PatchSeriesProcessor.LoadImage('Contone Image', patchID);
        mtImages{m, n}    = PatchSeriesProcessor.LoadImage('Monotone Image', patchID);
        
        patchHeight       = min([patchHeight  size(htImages{m,n}, 1)]);
        patchWidth        = min([patchWidth   size(htImages{m,n}, 2)]);
      catch err
        debugstamp;
      end
    end
  end
  
  bufferWidth             = 0; %round(patchWidth/3);
  
  blockWidth              = (patchWidth+bufferWidth)*blockColumns;
  blockHeight             = patchHeight*blockRows;
    
  htImage                 = ones(blockHeight, blockWidth) .* (100-meanTone)/100;
  ctImage                 = ones(blockHeight, blockWidth) .* (100-meanTone)/100;
  mtImage                 = ones(blockHeight, blockWidth);
  
  for m = 1:blockRows
    for n = 1:blockColumns
      htPatchImage        = im2double(htImages{m, n});
      ctPatchImage        = im2double(ctImages{m, n});
      mtPatchImage        = im2double(mtImages{m, n});
      
      if isempty(htPatchImage), continue; end;
      
      x1                  = 1+((n-1)*(patchWidth+bufferWidth));
      x2                  = x1+patchWidth-1;      
      y1                  = 1+((m-1)*patchHeight);
      y2                  = y1+patchHeight-1;
      
      try
        htImage(y1:y2, x1:x2)  = htPatchImage(1:patchHeight, 1:patchWidth);
        ctImage(y1:y2, x1:x2)  = ctPatchImage(1:patchHeight, 1:patchWidth);
        mtImage(y1:y2, x1:x2)  = mtPatchImage(1:patchHeight, 1:patchWidth);
      catch err
        debugStamp;
      end
    end
  end
  
  
  try
    pad                   = 10;
    paddedBlock           = zeros(size(htImage)+(pad*2));                    % blockHeight+20, blockWidth+20);
    paddedBlock(pad+1:pad+blockHeight, pad+1:pad+blockWidth) = htImage;
    htImage            = paddedBlock;
  catch err
    debugStamp;
  end
  
  return;
end
