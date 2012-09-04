classdef PrintParameters < handle & matlab.mixin.Copyable
  %PRINTPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    DotGain
    Noise
    Spread
    Unsharp
  end
  
  methods
    
    function obj = PrintParameters(dotGain, noise, spread, unsharp)
      if nargin==4
        obj.setParameters(dotGain, noise, spread, unsharp);
      end
    end
    
    function setParameters(obj, dotGain, noise, spread, unsharp)
      obj.DotGain = dotGain;
      obj.Noise = noise;
      obj.Spread = spread;
      obj.Unsharp = unsharp;
    end
    
  end
  
  properties (Dependent), Print; end
  methods, function obj = get.Print(obj), end, end
  
end

