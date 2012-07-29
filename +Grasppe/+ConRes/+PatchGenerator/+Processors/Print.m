classdef Print < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
  end
  
  methods
    
    function output = Run(obj)
      variables = obj.Variables;
      output = [];
      obj.Variables = variables;
      
    end
    
  end
  
end

