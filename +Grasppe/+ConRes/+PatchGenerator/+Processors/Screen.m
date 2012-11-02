classdef Screen < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (Transient)
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
      
      output        = obj.Input;
      variables     = obj.Variables;
      params        = obj.Parameters;
      
      screen        = eval(class(output)); %Grasppe.ConRes.PatchGenerator.Models.FourierImage;
      
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
      
      tone          = [];
      
      if isempty(tone)
        try tone      = output.Variables.Patch.Mean; end;
      end
      
      if isempty(tone)
        try tone      = output.ProcessData.Parameters.Mean; end;
      end
      
      if isempty(tone)
        try tone      = output.Variables.Process{1}.Mean; end;
      end
      
      if isempty(tone)
        error('Grasppe:ConResScreening:InvalidTone', 'Unable to locate the tone parameter in the input.');
      end
      
      try
        %% Generate Tone-Corrected Halftone Image
        screened    = [];
        meantone    = tone;
        tonestep    = -1;
        steplimit   = 0; % 5;
        tonelimit   = 5;
        tonediff    = 0;
        while isempty(screened) || (abs(meantone-tone)>tonelimit && tonestep<steplimit)
          tonestep  = tonestep+1;         
          screened  = grasppeScreen3(image-(tonediff/100), ppi, spi, lpi, theta, print);          
          meantone  = 100-mean(screened(:))*100;
          tonediff  = tonediff + tone - meantone;          
          %disp(['ToneCorrection: ' num2str([tonestep tone meantone tonediff]) ]);          
          %checks    = [ abs(meantone-tone) tonestep ];
        end
        image       = screened; %disp(['ToneCorrection: ' num2str([tonestep tone meantone tonediff]) ]);
      catch err
        debugStamp(err,1);
        rethrow(err);
      end
      
      
      try
        %% Generate Screen Image (Not Tone-Corrected)
        halftone  = halftone.*(tone/100);
        halftone  = grasppeScreen3(1-halftone, ppi, spi, lpi, theta, print);
        %halftone  = grasppeScreen3(1-halftone-(tonediff/100), ppi, spi, lpi, theta, print);
      catch err
        warning('Generating 50% halftone image because mean halftone failed to generate');
        halftone  = halftone.*(50/100);
        halftone  = grasppeScreen3(halftone, ppi, spi, lpi, theta, print);
        %halftone  = grasppeScreen3(halftone-(tonediff/100), ppi, spi, lpi, theta, print);
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
      parameters.TrueMean           = meantone;
      parameters.MeanOffset         = tonediff;
      
      variables.Screen  = parameters;
      
      obj.Variables = variables;
    end
  end
  
end

