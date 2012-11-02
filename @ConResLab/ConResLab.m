classdef ConResLab
  %CONRESLAB Grappe ConRes Reseach Tools
  %   Detailed explanation goes here
  
  properties
  end
  
  methods (Static)
    openPatchGenerator();
    runSeries();
    declareFunctions();
    compileProject();
    tallySRFData(data);
    exportSeries(data);
    exportPatches(data);
    exportBlocks(data);
    exportBlockComps(data);
    generateSeries(seriesID);
  end
  
end

