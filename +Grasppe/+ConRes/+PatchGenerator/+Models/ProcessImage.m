classdef ProcessImage < matlab.mixin.Copyable
  %IMAGEPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (Dependent)
    Width
    Height
    Depth
    Resolution
    Unit
    Mode
    Process
    ProcessData
    Image
    Fourier
    FourierImage
    Domain
  end
  
  properties
    Variables = struct;
    Snapshots = {};
    PlotFFT = true;
    fftimage;
    ffttimer;
    FundamentalFrequencies
  end
  
  properties (Access=private)
    width
    height
    depth
    resolution  = 2400;
    unit        = 'Inch';
    mode
    process     = {};
    processData = Grasppe.Occam.ProcessData.empty;
    image
    domain      = 'spatial';
    % variables   = struct;
  end
  
  methods
    
    function obj = ProcessImage()
      obj.ffttimer = timer('Tag','ImageProcessor', 'StartDelay', 1, ...
        'TimerFcn', @(x,y) obj.generatePlotFFT);
    end
    
    function delete(obj)
      try delete(obj.ffttimer); end
    end
    
    function width = get.Width(obj)
      width = obj.width;
    end
    
    function set.Width(obj, width)
      if ~isequal(obj.width, width)
        obj.width = width;
      end
    end
    
    function height = get.Height(obj)
      height = obj.height;
    end
    
    function set.Height(obj, height)
      if ~isequal(obj.height, height)
        obj.height = height;
      end
    end
    
    function depth = get.Depth(obj)
      depth = obj.depth;
    end
    
    function set.Depth(obj, depth)
      if ~isequal(obj.depth, depth)
        obj.depth = depth;
      end
    end
    
    function resolution = get.Resolution(obj)
      resolution = obj.resolution;
    end
    
    function set.Resolution(obj, resolution)
      if ~isequal(obj.resolution, resolution)
        obj.resolution = resolution;
      end
    end
    
    function unit = get.Unit(obj)
      unit = obj.unit;
    end
    
    function set.Unit(obj, unit)
      if ~isequal(obj.unit, unit)
        obj.unit = unit;
      end
    end
    
    function mode = get.Mode(obj)
      mode = obj.mode;
    end
    
    function set.Mode(obj, mode)
      if ~isequal(obj.mode, mode)
        obj.mode = mode;
      end
    end
    
    function process = get.Process(obj)
      process = obj.process;
    end
    
    %     function set.Process(obj, process)
    %       if ~isequal(obj.process, process)
    %         obj.process = process;
    %       end
    %     end
    
    function setImage(obj, image, resolution)
      obj.Image       = image;
      obj.Resolution  = resolution;
      
    end
    
    function processData = get.ProcessData(obj)
      processData = obj.processData;
    end
    
    function addProcess(obj, process)
      try
        processData = process.getProcessData;
        obj.process{end+1}      = process;
        obj.processData(end+1)  = processData;
      end
    end
    
    function image = get.Image(obj)
      image = obj.image;
    end
    
    function image = get.Fourier(obj)
      image = obj.image;
      if isreal(image)
        image = obj.forwardFFT(image);
      end
      %       switch (obj.Domain)
      %         case 'frequency'
      %         otherwise
      %           image = obj.forwardFFT(image);
      %       end
    end
    
    function image = get.FourierImage(obj)
      obj.generatePlotFFT();
      image = obj.fftimage;
      %if isempty(image)
      %obj.generatePlotFFT;
      %         image = obj.Image;
      %         switch (obj.Domain)
      %           case 'frequency'
      %           otherwise
      %             image = obj.forwardFFT(image);
      %         end
      %obj.fftimage = image;
      
      %image = obj.bandPlotFFT(image);
      %end
      
    end
    
    function image = forwardFFT(obj, image)
      % Sizing & Padding
      sP  = size(image,1);
      sQ  = size(image,2);
      nP  = 1-mod(sP,2);
      nQ  = 1-mod(sQ,2);
      fP  = ceil(2*(sP-nP));
      fQ  = ceil(2*(sQ-nQ));
      
      image = image(1:end-(nP),1:end-(nQ));
      
      image = fftshift(fft2(image, fP, fQ));
    end
    
    function generatePlotFFT(obj)
      
      %debugging = true;
      
      try
        stop(obj.ffttimer);
        fftdata = obj.Fourier;
        
        if isempty(fftdata)
          fftdata = obj.image;
        end
        
        if isreal(fftdata)
          fftdata = obj.forwardFFT(fftdata);
        end
        
        if isempty(obj.fftimage)
          obj.bandPlotFFT([]);
          fftimage = [];
          if prod(size(fftdata)) < 768*768
            while isempty(fftimage)
              fftimage = obj.bandPlotFFT(fftdata);
              pause(0.1);
            end
          else
            fftimage = fftdata;
            
            if size(fftimage,3) > 1
              fftimage = fftimage(:,:,1);
              dispdbg('Flattening Image...');
            end
            
            fftimage     = real(log(1+abs(fftimage)));
            imageMin     = min(fftimage(:)); imageMax = max(fftimage(:));
            fftimage     = (fftimage-imageMin) / (imageMax-imageMin);
          end
          
          obj.fftimage = fftimage;
        end
      catch err
        disp(err);
        return;
      end
    end
    
    function image = bandPlotFFT(obj, image)
      
      persistent  fxBusy idx; %hFig hAxis
      
      %debugging = true;
      
      dataColumn = 4;
      
      if isempty(image)
        fxBusy = false;
        %try delete(hAxis);  end
        %try delete(hFig);   end
        return;
      end
      
      if isequal(fxBusy, true)
        image = [];
        return;
      end
      
      try
        
        fxBusy=true;
        dispdbg('Generating Image...');
        R=tic;
        try
          
          if isempty(idx), idx = 1; 
          else idx = idx + 1; end
          
          try
            idx = evalin('base', 'BandIDX')+1;
          end
          
          assignin('base', 'BandIDX', idx);     
          
          image1 = image;
          
          if size(image1,3) > 1
            image1 = image1(:,:,1);
            dispdbg('Flattening Image...');
          end
          
          image1b = image1;
          
          image1     = real(log(1+abs(image1)));
          imageMin  = min(image1(:)); imageMax = max(image1(:));
          image1     = (image1-imageMin) / (imageMax-imageMin);
          
          if isequal(obj.PlotFFT, true)
            
            %if ~isscalar(hFig) || ~ishandle(hFig)
            hFig  = figure('Visible', 'off', 'Position',[-1000 -1000 300 300]);
            %  hAxis = [];
            %end
            
            %if ~isscalar(hAxis) || ~ishandle(hAxis)
            hAxis = axes('Parent', hFig); % 'Visible', 'off'
            %end
            
            dispdbg('Generating Plot...');
            
            [bFq fqData] = Grasppe.Kit.ConRes.CalculateBandIntensity(image1b); %image1
            
            %             baseData  = [];
            %             baseRow   = 1;
            %
            %             fqData(2:end+1, :)  = fqData;
            %             fqData(1, :)        = idx;
            %
            %             nBands = size(fqData, 1);
            %
            %             try
            %               baseData  = evalin('base', 'BandIntensityData');
            %               baseRow   = size(baseData,1)+1;
            %             end
            %
            %             baseData(baseRow:baseRow+nBands-1, :) = fqData;
            %
            %             assignin('base', 'BandIntensityData', baseData);
            
            bFq = fqData(:,dataColumn);
            
            % while (max(bFq)<1)
            %   bFq = bFq * 10;
            % end
            %
            % while (min(bFq)>1)
            %   bFq = bFq / 10;
            % end
            
            yR  = bFq/max(bFq(:));
            xR  = 1:numel(bFq);
            zR  = ones(size(xR));
            
            xF  = (7/2);
            xD  = 0.5 + floor((size(image1,2)/4));
            yD  = 0.5 + floor(size(image1,1)/2);
            
            yZ = 3;
            %yM = [max(yR) max(yR(yZ:end))]
            %yR = yR
            yR = (yR*100)-(max(yR(yZ:end)*100)/2);
            yM = nanmean(yR(yZ:end));
            yS = 5; %yM; %*2;            
            
            %hold on;
            
            cla(hAxis);
            
            image2 = repmat(image1, [1, 1, 3]);
            
            imshow(image2, 'Parent', hAxis);
            truesize(hFig);
            
            lOp = {'Parent', hAxis, 'LineWidth', 0.5, 'linesmoothing','on'};
            
            hold on;
            
            yN = 1;
            x  = []; y = [];
            for m = 1:5:numel(xR) %min(xR):5:max(xR)
              xv = [ 0 0]  + (xD+xR(m)*xF);
              if yN==1
                yv = [-35 35]  + yD + yM + yS; %+yR(m);
                yN = 0;
              else
                yv = [-25 25]  + yD + yM + yS; % +yR(m);
                yN = 1;
              end
              %x = [x xv];
              %y = [y yv];
              line(xv, yv, [0 0], 'Color', 'w', 'LineStyle', '-',   lOp{:}, 'LineWidth', 0.25);
              line(xv, yv, [0 0], 'Color', 'k', 'LineStyle', ':',  lOp{:});
            end
            %           % line(x+0.5, y, zeros(size(x)), 'Parent', hAxis, 'Color', 'w', 'Linewidth', 0.25, 'linesmoothing','on');            
            
            
            plot(hAxis, xD+xR*xF, yD+yR+yS, 'g', lOp{:}, 'Linewidth', 0.5);            
                        
            fQ = [obj.FundamentalFrequencies];
            
            if isnumeric(fQ) %&& ~isempty(fQ)
              for m = fQ
                yv = [-35 35]  + yD + yM + yS; %+yR(m);
                xv = [0 0] + xD + m*xF;
                line(xv, yv, [0 0], 'Color', 'r', lOp{:}, 'LineWidth', 4);
                
                % zv = max([-1 0 1] + yR(floor(m))); % yR(ceil(m))]);
                
                % text(mean(xv), max(yv)-1, 0, [num2str(m,'%3.1f') ' [' num2str(zv,'%3.1f') ']'], 'HorizontalAlignment', 'center', 'VerticalAlignment', 'top', 'Color', 'r', 'FontSize', 7);
                text(mean(xv), max(yv)-1, 0, [num2str(m,'%3.1f')], 'HorizontalAlignment', 'center', 'VerticalAlignment', 'top', 'Color', 'r', 'FontSize', 7);
              end
            end
                       
            if isnumeric(fQ) %&& ~isempty(fQ)
              fQ2 = fQ(1); % * [0.25 0.5 1 1.5]; %[0.25 0.5 0.75 1 1.25 1.5];
              for m = fQ2
                yv = [-20 20]  + yD + yM + yS; %+yR(m);
                xv = [0 0] + xD + m*xF;
                line(xv, yv, [0 0], 'Color', 'r', lOp{:}, 'LineWidth', 4);
                zv = max(bFq([-1:1]+floor(m))); % yR(ceil(m))]);
                
                text(mean(xv), min(yv)-15, 0, num2str(zv,'%3.2f'), 'HorizontalAlignment', 'center', 'VerticalAlignment', 'bottom', 'Color', 'g', 'FontSize', 7, 'FontWeight', 'bold');
                
                %text(mean(xv), max(yv)-1, 0, [num2str(m,'%3.1f') ' [' int2str(idx) ']'], 'HorizontalAlignment', 'center', 'VerticalAlignment', 'top', 'Color', 'r', 'FontSize', 6);
              end
            end
            
            
            dispdbg('Exporting Image...');
            
            image = export_fig(hFig); %, [1 1 size(image2,2) size(image2,1)]);
            
            delete(hAxis);
            delete(hFig);
          else
            image = image1;
          end
          
        catch err
          disp(err);
        end
        tocdbg(R);
      end
      
      fxBusy=false;
    end
    
    function image = inverseFFT(obj, image)
      % Unpadding
      sP  = size(image,1);
      sQ  = size(image,2);
      fP  = ceil(sP/2);
      fQ  = ceil(sQ/2);
      image = ifft2(ifftshift(image)); %, fP, fQ);
      image = image(1:fP, 1:fQ);
    end
    
    
    
    function set.Image(obj, image)
      if ~isequal(obj.image, image)
        obj.image = image;
        obj.updateMetadata();
      end
    end
    
    function Snap(obj, id)
      newObj = copy(obj);
      
      obj.Snapshots{end+1,1} = newObj; %.Image;
      
      obj.Snapshots{end,2}   = id;
      
      width   = obj.Width;
      height  = obj.Height;
      
      if (width*height < 600*600)
        start(newObj.ffttimer);
      else
        dispf('Not generating %d x %d', width, height);
      end
    end
    
    function updateMetadata(obj)
      image   = obj.image;
      
      width   = size(image, 2);
      height  = size(image, 2);
      depth   = ByteSize(image(1,1,1));
      
      mode    = obj.mode;
      
      switch size(image,3)
        case 1
          if depth < 1
            mode  = 'grayscale';
          else
            mode  = 'bitmap';
          end
        case 2
          mode = 'duotone';
        case 3
          if isempty(mode) || isequal(mode, 'rgb')
            mode = 'rgb';
          end
        case 4
          mode = 'cmyk';
        otherwise
          mode = 'multichannel';
      end
      
      obj.width   = width;
      obj.height  = height;
      obj.depth   = depth;
      obj.mode    = mode;
      
      
      obj.fftimage = [];
      
      %       obj.ffttimer = timer('Tag','ImageProcessor', 'StartDelay', 0.5, ...
      %         'TimerFcn', @(x,y) obj.generatePlotFFT);
      %      if (width*height < 600*600)
      %        start(obj.ffttimer);
      %      else
      %        dispf('Not generating %d x %d', width, height);
      %      end
      %obj.fftimage = obj.bandPlotFFT(image);
      
    end
    
    
    function domain = get.Domain(obj)
      domain = obj.domain;
    end
    
    function set.Domain(obj, domain)
      if ~isequal(obj.domain, domain)
        obj.domain = domain;
      end
    end
    
    % function variables = get.Variables(obj)
    %   variables = obj.variables;
    % end
    %
    % function set.Variables(obj, variables)
    %   if ~isequal(obj.variables, variables)
    %     obj.variables = variables;
    %   end
    % end
    
  end
  
  methods(Access = protected)
    % Override copyElement method:
    function cpObj = copyElement(obj)
      % Make a shallow copy of all four properties
      cpObj = copyElement@matlab.mixin.Copyable(obj);
      cpObj.fftimage = [];
      cpObj.bandPlotFFT([]);
      cpObj.ffttimer = timer('Tag','ImageProcessor', 'StartDelay', 1, ...
        'TimerFcn', @(x,y) obj.generatePlotFFT); %copy(obj.ffttimer);
      % Make a deep copy of the DeepCp object
      %cpObj.DeepObj = copy(obj.DeepObj);
    end
  end
  
end

