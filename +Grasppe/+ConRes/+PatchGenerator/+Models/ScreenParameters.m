classdef ScreenParameters < handle
  %SCREENPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Addressability
    Resolution
    PixelResolution
    Angle
    PrintParameters
  end
  
  methods
    
    function obj = ScreenParameters(addressability, resolution, angle)
      if nargin==3 
        obj.setParameters(addressability, resolution, angle);
      end
    end
    
    function setParameters(obj, addressability, resolution, angle)
      obj.Addressability = addressability;
      obj.Resolution = resolution;
      obj.Angle = angle;
    end
    
  end
  
end

