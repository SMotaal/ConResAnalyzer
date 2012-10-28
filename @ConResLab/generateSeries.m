function generateSeries( seriesID )
  %GENERATESERIES Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; %regexprep(eval(NS.CLASS), '\.\w+$', '.*'));
  import Grasppe.ConRes.Math;
  
  global ...
    forceRenderComposite forceOutputTable ...
    forceRenderPatches forceRenderBlocks ...
    forceGenerateImages forceGenerateStatistics forceGenerateFFT;
  
  
  forceAll                = true;
  forceRenderComposite    = forceAll;
  forceOutputTable        = forceAll;
  forceRenderPatches      = forceAll;
  forceRenderBlocks       = forceAll;
  forceGenerateImages     = forceAll;
  forceGenerateStatistics = forceAll;
  forceGenerateFFT        = forceAll;
  
  %% Prepare Series Parameters
  
  %% Generate Series
  
  seriesProcessor     = PatchSeriesProcessor();
  
  if exist('seriesID', 'var') && ischar(seriesID)
    seriesProcessor.SeriesID(seriesID);
  end
  
  data                = seriesProcessor.Run;
  
  
  %% Export Blocks
  ConResLab.exportPatches(data);
  
  %% Export Patches
  ConResLab.exportPatches(data);  
  
  %% Export Series
  ConResLab.exportSeries(data);
  
  
end

