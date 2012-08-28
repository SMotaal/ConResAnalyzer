classdef Scan < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
  end
  
  properties (Constant)
    DPI     = Grasppe.ConRes.Enumerations.ScanResolution';
    SCALE   = Grasppe.ConRes.Enumerations.ScanScale';
  end
  
  
  methods
    function output = Run(obj)
      
      import(eval(NS.CLASS));
      
      output  = obj.Input;
      variables       = obj.Variables;
      params  = obj.Parameters;
      
      dpi     = findField(params, 'resolution');
      scale   = findField(params, 'scale');
      
      spi     = output.Resolution;
      image   = output.Image;
      
      image   = imresize(image, (scale/100) * (dpi/spi), 'bicubic');
      
      parameters.(Scan.DPI)   = dpi;
      parameters.(Scan.SCALE) = scale;
      
      output.setImage(im2double(image), dpi);
      
      variables.Scan = parameters;
      
      obj.Variables = variables;
      
    end
  end
end
