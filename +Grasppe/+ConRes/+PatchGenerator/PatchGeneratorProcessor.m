classdef PatchGeneratorProcessor < Grasppe.Occam.Process
  %PATCHGENERATORPROCESSOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    PatchProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Patch;
    ScreenProcessor   = Grasppe.ConRes.PatchGenerator.Processors.Screen;
    PrintProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Print;
    ScanProcessor     = Grasppe.ConRes.PatchGenerator.Processors.Scan;
    UserProcessor     = Grasppe.ConRes.PatchGenerator.Processors.UserFunction;
    % DisplayProcessor  = Grasppe.ConRes.PatchGenerator.Processors.Display;
    View
  end
  
  methods
    
    function obj = PatchGeneratorProcessor()
      obj = obj@Grasppe.Occam.Process;
      obj.permanent = true;
      obj.addProcess(obj.PatchProcessor);
      obj.addProcess(obj.ScreenProcessor);
      obj.addProcess(obj.PrintProcessor);
      obj.addProcess(obj.ScanProcessor);
      % obj.addProcess(obj.DisplayProcessor);
    end
    
    function output = Run(obj)
      output  = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      panel   = obj.View;
      
      patchProcessor    = obj.PatchProcessor;
      screenProcessor   = obj.ScreenProcessor;
      printProcessor    = obj.PrintProcessor;
      scanProcessor     = obj.ScanProcessor;
      
      parameters      = obj.Parameters;
      
      
      % try screenProcessor.Parameters = parameters.Screen; end
      try printProcessor.Parameters = parameters.Print; end
      try scanProcessor.Parameters = parameters.Scan; end
      
      % ffScreening( contone, resolution, title, spi, lpi, dpi, angles, noise)
      
      % try
      %   % Printing
      %   tvi             = findField(parameters.Print, 'gain');
      %   noise           = findField(parameters.Print, 'noise');
      %   blur            = findField(parameters.Print, 'blur');
      %   spread          = findField(parameters.Print, 'radius');
      %
      %   % Scanning
      %   scanpdi         = findField(parameters.Scan, 'resolution');
      %   scanscale       = findField(parameters.Scan, 'scale');
      %
      %
      %   % Overloads
      %   width           = 256;
      % end
      
      panel.clearAxes();
      
      try
        
        %% Fix size
        % parameters.Patch.Size = parameters.Patch.Size/
        
        %% Generate Patch
        patchProcessor.Execute(parameters.Patch);
        patchImage = output.Image;
        
        
        %% Screen & Print Patch
        parameters.Screen.PrintProcessor  = printProcessor;
        parameters.Screen.PixelResolution = patchProcessor.Addressability;
        parameters.Screen.PrintParameters = parameters.Print;
        
        screenProcessor.Execute(parameters.Screen);
        screenedImage = output.Image;
        
        %% Scan Patch
        scanProcessor.Execute(parameters.Scan);
        scannedImage = output.Image;
      catch err
        disp(err);        
      end
      
      try
        %% Functions
        
        fxNames     = fieldnames(parameters.Processors);
        fxProcessor = obj.UserProcessor;
        
        for fxName = fxNames'
          fx = parameters.Processors.(char(fxName));
          fxProcessor.Parameters = fx;
          fxProcessor.Execute;
          disp(fx);
        end
        
      end
      
      
      try
        
        image = output.Image;
        
        
        if isequal(output.Domain,'frequency')
          image     = real(log(1+abs(image)));
          imageMin  = min(image(:)); imageMax = max(image(:));
          image     = (image-imageMin) / (imageMax-imageMin);
        end
        %elseif output.Domain = 'spatial'
        
        panel.setImage(image);
%         panel.setImage(image);
%         panel.setImage(image);
        
        set(gcf,'Name','ConRes');
      catch err
        disp(err);
      end
      
      drawnow expose update;
      % imshow(imfilter(im2double(grasppeScreen3(img, addressability)), H),'InitialMagnification', 100); set(gcf,'Name',toString(s)); drawnow expose update;
    end
  end
  
end

