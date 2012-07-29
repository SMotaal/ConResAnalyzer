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
      %       obj.PatchProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Patch;
      %       obj.ScreenProcessor   = Grasppe.ConRes.PatchGenerator.Processors.Screen;
      %       obj.PrintProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Print;
      %       obj.ScanProcessor     = Grasppe.ConRes.PatchGenerator.Processors.Scan;
      %       obj.UserProcessor     = Grasppe.ConRes.PatchGenerator.Processors.UserFunction;
      obj.permanent = true;
      obj.addProcess(obj.PatchProcessor);
      obj.addProcess(obj.ScreenProcessor);
      obj.addProcess(obj.PrintProcessor);
      obj.addProcess(obj.ScanProcessor);
      obj.addProcess(obj.UserProcessor);
      % obj.addProcess(obj.DisplayProcessor);
    end
    
    function addProcess(obj, process)
      obj.addProcess@Grasppe.Occam.Process(process);
      process.Controller  = obj;
      process.View        = obj.View;
    end
    
    function output = Run(obj)
      output      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      reference   = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      patch       = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      screen      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      
      panel       = obj.View;
      
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
        screenImage = screenProcessor.HalftoneImage;
        
        %% Scan Patch
        scanProcessor.Execute(parameters.Scan);
        scannedImage = output.Image;
        
        %% Patch & Reference Images
        referenceImage = imresize(patchImage, size(scannedImage));
        screenImage = imresize(screenImage, size(scannedImage));
        
        patch.setImage(scannedImage, output.Resolution);
        reference.setImage(referenceImage, output.Resolution);
        screen.setImage(screenImage, output.Resolution);        
        
        output.Variables.PatchImage     = patch;
        output.Variables.ReferenceImage = reference;
        output.Variables.HalftoneImage  = screen;
        
        output.snap('GeneratedPatch');
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
          id = char(fxName);
          try id = char(fx.Expression); end
          output.snap(id);
        end
        
      end
      
      
      try
        
        images  = output.Snapshots(:,1);
        ids     = output.Snapshots(:,2);
        
        images{end+1} = output.Image;
        ids{end+1}    = 'FinalOutput';
        
        images{end+1} = output.Fourier;
        ids{end+1}    = 'FinalFFT';
        
        tXY = {0.5, 0.95};
        tOp = {'Units', 'normalized', ...
          'HorizontalAlignment', 'center', ...
          'VerticalAlignment', 'middle', ...
          'BackgroundColor', [1 1 1], ...
          'FontSize', 6, 'FontName', 'DIN', 'FontUnits', 'points'};
        
        for m = 1:numel(images)-2
          image = images{m};
          
          fftMode = ~isreal(image);
          
          if fftMode
            panel.setImage(output.inverseFFT(image));
            try
              T   = [ids{m} ' inv'];
              hT  = text(tXY{:}, T, 'Parent', gca, tOp{:});
              alpha(hT, 0.75);
            end
            image     = real(log(1+abs(image)));
            imageMin  = min(image(:)); imageMax = max(image(:));
            image     = (image-imageMin) / (imageMax-imageMin);
          end
          panel.setImage(image);
          
          try
            T   = [ids{m}];
            hT  = text(tXY{:}, T, 'Parent', gca, tOp{:});
            alpha(hT, 0.75);
          end
          
          
          if ~fftMode
            image     = output.forwardFFT(image);
            image     = real(log(1+abs(image)));
            imageMin  = min(image(:)); imageMax = max(image(:));
            image     = (image-imageMin) / (imageMax-imageMin);
            
            panel.setImage(image);
            try
              T   = [ids{m} ' fwd'];
              hT  = text(tXY{:}, T, 'Parent', gca, tOp{:});
              alpha(hT, 0.75);
            end
          end
          %ti(ids{m});
        end
        
        %         image = output.Image;
        %
        %
        %         if isequal(output.Domain,'frequency')
        %           image     = real(log(1+abs(image)));
        %           imageMin  = min(image(:)); imageMax = max(image(:));
        %           image     = (image-imageMin) / (imageMax-imageMin);
        %         end
        %elseif output.Domain = 'spatial'
        
        %panel.setImage(image);
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

