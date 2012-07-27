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
      
      parameters.(CONRES.PPI')      = ppi;
      parameters.(CONRES.SPI')      = spi;
      parameters.(CONRES.LPI')      = lpi;
      parameters.(CONRES.Angle')    = theta;      
      
      printParams.(CONRES.TVI')     = print.Gain;
      printParams.(CONRES.Noise')   = print.Noise;
      printParams.(CONRES.Radius')  = print.Radius;
      printParams.(CONRES.Blur')    = print.Blur;
      
      output.setImage(im2double(image), spi);
      
      variables.Print   = printParams;
      variables.Screen  = parameters;
      
      obj.Variables = variables;
    end
  end
  
end

