classdef Screen < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    HalftoneImage
  end
  
  properties (Constant)
    PPI     = Grasppe.ConRes.Enumerations.PPI';
    SPI     = Grasppe.ConRes.Enumerations.SPI';
    LPI     = Grasppe.ConRes.Enumerations.LPI';
    ANGLE   = Grasppe.ConRes.Enumerations.Angle';
    
    TVI     = Grasppe.ConRes.Enumerations.TVI';
    NOISE   = Grasppe.ConRes.Enumerations.Noise';
    RADIUS	= Grasppe.ConRes.Enumerations.Radius';
    BLUR    = Grasppe.ConRes.Enumerations.Blur';
  end
  
  methods
    function output = Run(obj)
      
      import Grasppe.*;
      
      import(eval(NS.CLASS));
      
      screen        = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
      
      output        = obj.Input;
      variables     = obj.Variables;
      params        = obj.Parameters;
      
      ppi           = findField(params, Screen.PPI);    % 'pixelresolution');
      spi           = findField(params, Screen.SPI);    % 'addressability');
      lpi           = findField(params, Screen.LPI);    % 'resolution');
      theta         = findField(params, Screen.ANGLE);  % 'angle');
      
      gain          = findField(params, Screen.TVI);
      noise         = findField(params, Screen.NOISE);
      radius        = findField(params, Screen.RADIUS);
      blur          = findField(params, Screen.BLUR);
      
      % print   = findField(params, 'PrintParameters');
      print         =  struct( ...
        'Gain', gain, 'Noise', noise, 'Radius', radius, 'Blur', blur ...
        );
      
      image         = output.Image;
      
      halftone      = ones(size(image));
      
      tone          = output.ProcessData.Parameters.Mean;
      
      try
        screened    = [];
        meantone    = tone;
        tonestep    = -1;
        tonediff    = 0;
        while isempty(screened) || (abs(meantone-tone)>0.5 && tonestep<=5)
          tonestep  = tonestep+1;          
          tonediff  = tonediff + tone - meantone;
          %disp([tonestep tone meantone tonediff]);      
          screened  = grasppeScreen3(image-(tonediff/100), ppi, spi, lpi, theta, print);
          meantone  = 100-mean(screened(:))*100;
        end
        %disp([tonestep tone meantone tonediff]);
        disp(['ToneCorrection: ' num2str([tonestep tone meantone tonediff]) ]);
        image       = screened;
        %tone  = tone + tonediff;
      catch err
        debugStamp(err,1);
        rethrow(err);
      end
      
      
      try
        %        tone      = output.ProcessData.Parameters.Mean;
        halftone  = halftone.*(tone/100);
        halftone  = grasppeScreen3(1-halftone-(tonediff/100), ppi, spi, lpi, theta, print);
      catch err
        warning('Generating 50% halftone image because mean halftone failed to generate');
        halftone  = halftone.*(50/100);
        halftone  = grasppeScreen3(halftone-(tonediff/100), ppi, spi, lpi, theta, print);
      end
      
      screen.setImage(im2double(halftone), spi);
      obj.HalftoneImage = screen;
      %output.Variables.halftoneImage = halftone;
      
      parameters.(Screen.PPI)       = ppi;
      parameters.(Screen.SPI)       = spi;
      parameters.(Screen.LPI)       = lpi;
      parameters.(Screen.ANGLE)     = theta;
      
      parameters.(Screen.TVI)       = gain;
      parameters.(Screen.NOISE)     = noise;
      parameters.(Screen.RADIUS)    = radius;
      parameters.(Screen.BLUR)      = blur;
      
      output.setImage(im2double(image), spi);
      
      %variables.Print   = printParams;
      variables.Screen  = parameters;
      
      obj.Variables = variables;
    end
  end
  
end

