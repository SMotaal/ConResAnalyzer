classdef Stepper < handle
  %STEPPER Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    steps = 0;
  end
  
  methods
    
    % function obj = Stepper()
    %   obj.reset;
    % end
    
    function reset(obj)
      obj.steps   = 0;
    end
    
    function steps = step(obj)
      obj.steps   = obj.steps+1;
      
      if nargout>0, steps = obj.steps; end
    end
  end
  
end

