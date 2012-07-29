classdef Scan < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
  end
  
  methods
    function output = Run(obj)
      output  = obj.Input;
      variables       = obj.Variables;
      params  = obj.Parameters;
      
      dpi     = findField(params, 'resolution');
      scale   = findField(params, 'scale');
      
      spi     = output.Resolution;
      image   = output.Image;
      
      image   = imresize(image, (scale/100) * (dpi/spi), 'bicubic');
      
      parameters.(Grasppe.ConRes.Enumerations.DPI')    = dpi;
      parameters.(Grasppe.ConRes.Enumerations.Scale')  = scale;
      
      output.setImage(im2double(image), dpi);
      
      variables.Scan = parameters;
      
      obj.Variables = variables;
      
    end
  end
end
