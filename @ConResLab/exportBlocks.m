function [ output_args ] = exportPatches(data) % SRF, Series )
  %TALLYSRFDATA Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  import Grasppe.ConRes.Math;
  
  global forceRenderBlocks;
  
  forceRenderBlocks = false;
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
  baseParameters            = struct(data.Series.Parameters(1))
  paths                     = htPaths(:,1);
  
  
  baseParameters.Screen.Resolution = 175;
  %baseParameters.Patch = rmfield(baseParameters.Patch, {'Mean', 'Contrast', 'Resolution'});
  
  meanToneSteps             = numel(meanToneRange);
  
  for m = 1:meanToneSteps
    meanTone                = meanToneRange(m);
    
    params                  = baseParameters;
    params.Patch.Mean       = meanTone;
    
    blockParams             = params;
    blockParams.Patch       = rmfield(blockParams.Patch, {'Contrast', 'Resolution'});
    blockId                 = PatchSeriesProcessor.GetParameterID(blockParams);
    blockGrid               = generateBlockGrid(meanTone, contrastRange, resolutionRange, params, seriesTable);
    blockImage              = assembleBlockImage(blockGrid, meanTone);
    
    PatchSeriesProcessor.SaveImage(blockImage, 'BlockImage', blockId);
    
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
      
      % for m = 1:numel(data.Series.Parameters), find(strcmpi(data.Series.Table(:,4), PatchSeriesProcessor.GetParameterID(data.Series.Parameters(m)))), end
      
      patchID                 = PatchSeriesProcessor.GetParameterID(params);
      
      disp(patchID);
      %patchRow                = find(strcmpi(seriesTable(:,4), patchID));
      %patchPath               = seriesTable(patchRow, 1);
      
      if ~any(strcmpi(seriesTable(:,4), patchID)), continue; end;
      
      blockGrid{m, n}         = patchID; %patchPath;
    end
  end
end

function blockImage = assembleBlockImage(blockGrid, meanTone)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor  
  
  blockImages             = cell(size(blockGrid));
  
  patchWidth              = [];
  patchHeight             = [];
  
  blockRows               = size(blockGrid,1);
  blockColumns            = size(blockGrid,2);
  
  for m = 1:blockRows
    for n = 1:blockColumns
      patchID             = blockGrid{m,n};
      
      if isempty(patchID), continue; end;
      
      try
        blockImages{m,n}  = PatchSeriesProcessor.LoadImage('Halftone Image', patchID);
        
        patchHeight       = min([patchHeight  size(blockImages{m,n}, 1)]);
        patchWidth        = min([patchWidth   size(blockImages{m,n}, 2)]);
      catch err
      end
    end
  end
  
  bufferWidth             = round(patchWidth/3);
  
  blockWidth              = (patchWidth+bufferWidth)*blockColumns;
  blockHeight             = patchHeight*blockRows;
    
  blockImage              = ones(blockHeight, blockWidth) .* (100-meanTone)/100;
  
  for m = 1:blockRows
    for n = 1:blockColumns
      patchImage          = im2double(blockImages{m, n});
      if isempty(patchImage), continue; end;
      
      x1                  = 1+((n-1)*(patchWidth+bufferWidth));
      x2                  = x1+patchWidth-1;      
      y1                  = 1+((m-1)*patchHeight);
      y2                  = y1+patchHeight-1;
      
      try
        blockImage(y1:y2, x1:x2)  = patchImage(1:patchHeight, 1:patchWidth);
        
      catch err
        debugStamp;
      end
    end
  end
  
  
  try
    pad                   = 10;
    paddedBlock           = zeros(size(blockImage)+(pad*2));                    % blockHeight+20, blockWidth+20);
    paddedBlock(pad+1:pad+blockHeight, pad+1:pad+blockWidth) = blockImage;
    blockImage            = paddedBlock;
  catch err
    debugStamp;
  end
  
  return;
end
