classdef Screen < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    
  end
  
  methods
    function output = Run(obj)
      
      import Grasppe.*;
      
      output          = obj.Input;
      variables       = obj.Variables;
      params  = obj.Parameters;
      
      ppi     = findField(params, 'pixelresolution');
      spi     = findField(params, 'addressability');
      lpi     = findField(params, 'resolution');
      theta   = findField(params, 'angle');
      print   = findField(params, 'PrintParameters');
      
      image   = output.Image;
      
      image   = grasppeScreen3(image, ppi, spi, lpi, theta, print);
      
      parameters.(Grasppe.ConRes.Enumerations.PPI')      = ppi;
      parameters.(Grasppe.ConRes.Enumerations.SPI')      = spi;
      parameters.(Grasppe.ConRes.Enumerations.LPI')      = lpi;
      parameters.(Grasppe.ConRes.Enumerations.Angle')    = theta;      
      
      printParams.(Grasppe.ConRes.Enumerations.TVI')     = print.Gain;
      printParams.(Grasppe.ConRes.Enumerations.Noise')   = print.Noise;
      printParams.(Grasppe.ConRes.Enumerations.Radius')  = print.Radius;
      printParams.(Grasppe.ConRes.Enumerations.Blur')    = print.Blur;
      
      output.setImage(im2double(image), spi);
      
      variables.Print   = printParams;
      variables.Screen  = parameters;
      
      obj.Variables = variables;
    end
  end
  
end

