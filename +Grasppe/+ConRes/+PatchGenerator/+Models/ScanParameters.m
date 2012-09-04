classdef ScanParameters < handle & matlab.mixin.Copyable
  %SCREENPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Resolution
    Scale
  end
  
  methods
    
    function obj = ScanParameters(resolution, scale)
      if nargin==2
        obj.setParameters(resolution, scale);
      end
    end
    
    function setParameters(obj, resolution, scale)
      obj.Resolution = resolution;
      obj.Scale = scale;
    end
    
  end
  
  
  properties (Dependent), Scan; end
  methods, function obj = get.Scan(obj), end, end  
end
  
