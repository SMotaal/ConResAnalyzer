classdef PatchParameters < handle
  %PATCHPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    MeanTone
    Contrast
    Resolution
    Size
  end
  
  methods
    
    function obj = PatchParameters(meanTone, contrast, resolution, size)
      if nargin==4
        obj.setParameters(meanTone, contrast, resolution, size);
      end
    end
    
    function setParameters(obj, meanTone, contrast, resolution, size)
      obj.MeanTone = meanTone;
      obj.Contrast = contrast;
      obj.Resolution = resolution;
      obj.Size = size;
    end
    
  end
  
end

