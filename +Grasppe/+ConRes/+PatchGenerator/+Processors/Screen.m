classdef Screen < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    
  end
  
  methods
    function output = Run(obj)
      output  = obj.Input;
      params  = obj.Parameters;
      
      ppi     = findField(params, 'pixelresolution');
      spi     = findField(params, 'addressability');
      lpi     = findField(params, 'resolution');
      theta   = findField(params, 'angle');
      print   = findField(params, 'PrintParameters');
      
      image   = output.Image;
      
      image   = grasppeScreen3(image, ppi, spi, lpi, theta, print);
      
      output.setImage(im2double(image), spi);
      
    end
  end
  
end

