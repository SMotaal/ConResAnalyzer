function [ output_args ] = exportBlock(data) % SRF, Series )
  %TALLYSRFDATA Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  import Grasppe.ConRes.PatchGenerator.Processors.*;
  import Grasppe.ConRes.Math;
  import Grasppe.ConRes.FX;
  
  global forceRenderBlocks;
  
  forceRenderBlocks             = isequal(forceRenderBlocks, true); %false;
  
  RTV                           = Patch.MEANTONE;
  CON                           = Patch.CONTRAST;
  RES                           = Patch.RESOLUTION;
  MM                            = Patch.SIZE;
  SPI                           = Screen.SPI;
  LPI                           = Screen.LPI;
  DEG                           = Screen.ANGLE;
  DPI                           = Scan.DPI;
  SCL                           = Scan.SCALE;
  
  fRTV                          = '\tRTV: %5.2f';
  fSPI                          = '\tSPI: %5.2f';
  fLPI                          = '\tLPI: %5.2f';
  fDEG                          = '\tDEG: %5.2f';
  fDPI                          = '\tDPI: %5d';
  fRF                           = '\tRTV: %5.2f';
  tab                           = '\t';
  
  
  if ~exist('data', 'var')
    data                        = PatchSeriesProcessor.LoadData();
    %     data.SRF                    = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
    %     data.PRF                    = PatchSeriesProcessor.LoadData('PRF', 'PRFData');
  end
  
  seriesTable                   = data.Series.Table;
  
  parameters                    = struct(data.Series.Parameters(1));
  
  meanToneRange                 = data.Parameters.Patch.Mean;
  contrastRange                 = data.Parameters.Patch.Contrast;
  resolutionRange               = data.Parameters.Patch.Resolution;
  
  spiRange                      = data.Parameters.Screen.(SPI);
  lpiRange                      = unique([100 175 data.Parameters.Screen.(LPI)]);
  degRange                      = data.Parameters.Screen.(DEG);
  
  meanToneSteps                 = numel(meanToneRange);
  spiSteps                      = numel(spiRange);
  lpiSteps                      = numel(lpiRange);
  degSteps                      = numel(degRange);
  
  retinaFactor                  = 3;
  
  for m = 1:meanToneSteps
    meanTone                    = meanToneRange(m);
    
    params                      = parameters;
    
    dpi                         = params.Scan.(DPI);
    scl                         = params.Scan.(SCL);
    
    retinalAccuity              = Math.VisualResolution(dpi*scl/100); %seriesVariables.PixelAcuity;
    retina                      = @(x) FX.Retina(x, retinalAccuity, retinaFactor);
    
    params.Patch.Mean           = meanTone;
    
    blockParams                 = params;
    blockParams.Patch           = rmfield(blockParams.Patch, {CON, RES});
    
    ctBlockID                   = PatchSeriesProcessor.GetParameterID(blockParams, 'Contone Block');
    
    [ctPath, ctExists]          = PatchSeriesProcessor.GetResourcePath('BlockImage', [ctBlockID '-CT'], 'png');
    
    if ~ctExists
      dispf(['Generating Contone Block:' fRTV fDPI], meanTone, dpi);  %dispf('Generating Contone Block:\tRTV: %5.0f\tCON: %5.1f\tRES: %5.2f\tDPI: %5d', ...
      
      ctPatchParams             = params;
      ctPatchParams.Screen      = rmfield(ctPatchParams.Screen, {LPI, DEG});
      
      blockGrid                 = generateBlockGrid(meanTone, contrastRange, resolutionRange, ctPatchParams, seriesTable);
      ctBlockImage              = assembleBlockImage(blockGrid, meanTone);
      PatchSeriesProcessor.SaveImage(ctBlockImage, 'BlockImage', [ctBlockID '-CT']);
    else
      dispf(['Loading Contone Block:' fRTV fDPI], meanTone, dpi);  %dispf('Generating Contone Block:\tRTV: %5.0f\tCON: %5.1f\tRES: %5.2f\tDPI: %5d', ...
      ctBlockImage              = PatchSeriesProcessor.LoadImage(ctPath); % 'BlockImage', [blockId '-CT']);
    end
    
    dispf(['Filtering Contone Block:' fRTV fDPI fRF], meanTone, dpi, retinaFactor);  %dispf('Generating Contone Block:\tRTV: %5.0f\tCON: %5.1f\tRES: %5.2f\tDPI: %5d', ...
    PatchSeriesProcessor.SaveImage(retina(ctBlockImage), 'BlockImage', [ctBlockID '-CT-RF' int2str(retinaFactor)]);
    
    for n = 1:degSteps
      deg                       = degRange(n);
      for o = 1:spiSteps
        spi                     = spiRange(o);
        for p = 1:lpiSteps
          
          lpi                   = lpiRange(p);
          
          htParams              = blockParams;
          htParams.Screen.(SPI) = spi;
          htParams.Screen.(LPI) = lpi;
          htParams.Screen.(DEG) = deg;
          htBlockID             = PatchSeriesProcessor.GetParameterID(htParams, 'Halftone Block');
          
          [htPath, htExists]    = PatchSeriesProcessor.GetResourcePath('BlockImage', [htBlockID '-HT'], 'png');
          
          if ~htExists || ~ctExists
            dispf(['Generating Halftone Block:' fRTV fDPI fSPI fLPI fDEG], meanTone, dpi, spi, lpi, deg);
            
            halftoneImage       = imresize(grasppeScreen3(ctBlockImage, dpi, spi, lpi, deg), dpi/spi);
            PatchSeriesProcessor.SaveImage(halftoneImage, 'BlockImage', [htBlockID '-HT']);
          else
            dispf(['Loading Halftone Block:' fRTV fDPI fSPI fLPI fDEG], meanTone, dpi, spi, lpi, deg);
            halftoneImage       = PatchSeriesProcessor.LoadImage(htPath); % 'BlockImage', [blockId '-HT']);
          end
          
          dispf(['Filtering Halftone Block:' fRTV fDPI fSPI fLPI fDEG fRF], meanTone, dpi, spi, lpi, deg, retinaFactor);
          
          PatchSeriesProcessor.SaveImage(retina(halftoneImage), 'BlockImage', [htBlockID '-HT-RF' int2str(retinaFactor)]);
          
        end
      end
    end
    
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
      
      patchID                 = PatchSeriesProcessor.GetParameterID(params, 'Contone');
      
      if ~any(strcmpi(seriesTable(:,5), patchID)), continue; end;
      
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
        blockImages{m,n}  = PatchSeriesProcessor.LoadImage('Contone Image', patchID);
        
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
    debugStamp(err);
  end
  
  return;
end
