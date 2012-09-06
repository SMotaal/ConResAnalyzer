classdef PatchSeriesProcessor < Grasppe.ConRes.PatchGenerator.PatchGeneratorProcessor
  %PATCHSERIESPROCESSOR Run patch generator series
  %   Detailed explanation goes here
  
  properties
    Codes
  end
  
  methods
    
    output = Run(obj)
    
    updateTasks(obj, activeTask, taskTitle, numRender, numAnalyze, numExport, numCompile);
    prepareTasks(obj, title, numPrep, numRender, numAnalyze, numExport, numCompile);
    
    
    parameters          = PrepareParameters(obj);
        
  end
  
  methods (Static)
    str                 = GetParameterID(parameters, type);
    str                 = GetParameterString(parameters);
    fieldOutput         = GetFieldData(fieldName, overwriteFields, output);
    
    [pth exists folder] = GetResourcePath(type, id, ext)
    
    pth                 = SaveData(output, filename);
    output              = LoadData(field, filename);
    
    pth                 = SaveImage(img, type, id);
    img                 = LoadImage(type, id);
        
    fields              = ProcessSeriesFields(parameters);
    grids               = GenerateSeriesGrids(fields);
    
    series              = GenerateSeriesImages(grids, fields, processors, parameters, task);
    series              = GenerateSeriesFFT(series, grids, fields, processors, parameters, task)
    stats               = GenerateSeriesStatistics(series, grids, fields, processors, parameters, task)
    output              = Sandbox(varargin);
    
    [FFT SRF FIMG IMG]  = ProcessImageFourier(imagePaths, bandParameters)
  end
end
