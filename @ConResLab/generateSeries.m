function generateSeries( seriesID )
  %GENERATESERIES Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; %regexprep(eval(NS.CLASS), '\.\w+$', '.*'));
  import Grasppe.ConRes.Math;
  
  global ...
    forceRenderComposite forceOutputTable ...
    forceRenderPatches forceRenderBlocks ...
    forceGenerateImages forceGenerateStatistics forceGenerateFFT;
  
  
  forceAll                = false;
  forceRenderComposite    = forceAll;
  forceOutputTable        = forceAll;
  forceRenderPatches      = forceAll;
  forceRenderBlocks       = forceAll;
  forceGenerateImages     = forceAll;
  forceGenerateStatistics = forceAll;
  forceGenerateFFT        = forceAll;
  
  %% Prepare Series Parameters
  
  %% Generate Series
  
  seriesProcessor         = PatchSeriesProcessor();
  
  if exist('seriesID', 'var') && ischar(seriesID)
    seriesProcessor.SeriesID(seriesID);
    
    if ~isequal(seriesProcessor.SeriesID, seriesID)
      error('Grasppe:ConRes:SeriesIDError', ' Failed to set series ID.');
      return;
    end
  end
  
  data                    = seriesProcessor.Run;
  
  
  %% Export Blocks
  ConResLab.exportBlocks(data);
  % ConResLab.exportBlockComps(data);
  
  %% Export Patches
  ConResLab.exportPatches(data);  
  
  %% Export Series
  data.SRF                = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
  data.PRF                = PatchSeriesProcessor.LoadData('PRF', 'PRFData');
  
  ConResLab.exportSeries(data);
  
  
end

