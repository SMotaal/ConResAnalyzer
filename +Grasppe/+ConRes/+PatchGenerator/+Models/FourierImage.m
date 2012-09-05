classdef FourierImage < Grasppe.ConRes.PatchGenerator.Models.ProcessImage
  %IMAGEPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (Dependent)
    FFTData
    FFTImage
    Domain
  end
  
  properties
    FundamentalFrequencies
  end
  
  properties (Access=protected)
    domain	= 'spatial';
    fftData;
    fftImage;
    fftFundamental;
    
    PLOTFFT = true;
  end
  
  properties (Access=private)
    renderDelayTimer; 
  end
  
  methods
    
%     function obj = FourierImage()
%       obj@Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
%       obj.resetRenderTimer;
%     end
    
    function delete(obj)
      try stop(obj.renderDelayTimer);   end
      try delete(obj.renderDelayTimer); end
    end
        
    function fftData = get.FFTData(obj)
      
      fftData   = obj.fftData;
      image     = obj.image;
      
      if isempty(fftData) 
        if isreal(image)
          fftData = obj.forwardFFT(image);
        else
          fftData = image;
        end
        obj.fftData = fftData;
      end
      
    end
    
    function image = get.FFTImage(obj)
      if isempty(obj.fftImage)
        obj.renderDataPlot();
      end
      image = obj.fftImage;
    end
    
    function img = forwardFFT(obj, img)
      sP  = size(img,1);
      sQ  = size(img,2);
      nP  = 1-mod(sP,2);
      nQ  = 1-mod(sQ,2);
      fP  = ceil(2*(sP-nP));
      fQ  = ceil(2*(sQ-nQ));
      
      img = img(1:end-(nP),1:end-(nQ));
      img = fftshift(fft2(img, fP, fQ));
      
    end
    
    function img = inverseFFT(obj, img)
      fP  = ceil(size(img,1)/2);
      fQ  = ceil(size(img,2)/2);
      img = ifft2(ifftshift(img), fP, fQ);
    end
    
    function img = Real(obj, img)
      if ~isreal(img)
        img       = real(log(1+abs(img)));
        fftMin    = min(img(:)); imageMax = max(img(:));
        img       = (img-fftMin) / (imageMax-fftMin);
      end
    end
    
    function renderDataPlot(obj)

      try
        try stop(obj.renderDelayTimer); end
        fftData = obj.FFTData;
        
        if isempty(fftData)
          fftData = obj.image;
        end
        
        if isreal(fftData)
          fftData = obj.forwardFFT(fftData);
        end
        
        if isempty(obj.fftImage)
          obj.bandPlotFFT([]);
          fftImage = [];
          if prod(size(fftData)) < 768*768*2
            while isempty(fftImage)
              fftImage = obj.bandPlotFFT(fftData);
              pause(0.1);
            end
          else
            fftImage = fftData;
            
            if size(fftImage,3) > 1
              fftImage = fftImage(:,:,1);
              DBG.dispdbg('Flattening Image...');
            end
            
            fftImage = obj.Real(fftImage);
          end
          
          obj.fftImage = fftImage;
        end
      catch err
        debugStamp(err, 1);
        return;
      end
    end
    
    function img = bandPlotFFT(obj, img)
      
      persistent  fxBusy;
            
      dataColumn = 2;
      
      renderer = 'opengl';
      
      if isempty(img)
        fxBusy = false;
        return;
      end
      
      if isequal(fxBusy, true)
        img = [];
        return;
      end
      
      try
        
        fxBusy=true;
        DBG.dispdbg('Generating Image...');
        R=tic;
        try
          
          image1 = img;
          
          if size(image1,3) > 1
            image1 = image1(:,:,1);
            DBG.dispdbg('Flattening Image...');
          end
          
          image1b   = image1;
                    
          image1    = obj.Real(image1);
          
          width     = size(image1,2);
          height    = size(image1,1);
          
          if isequal(obj.PLOTFFT, true)
            
            hFig  = figure('Visible', 'off', 'Position', [-1000 -1000 width height], 'HandleVisibility','callback', 'Renderer', 'painters');

            hAxis = axes('Parent', hFig);
            
            DBG.dispdbg('Generating Plot...');
            
            [bFq fqData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(image1b)); 
                        
            bFq = fqData(:,dataColumn);
            
            yR  = bFq/max(bFq(:));
            yR2 = (1-yR)*0.5;
            xR  = 1:numel(bFq);
            zR  = ones(size(xR));
            
            xF  = (7/2);
            xD  = 0.5 + floor((size(image1,2)/4));
            yD  = 0.5 + floor(size(image1,1)/2);
            
            yZ  = 3;
            yR  = (yR*100)-(max(yR(yZ:end)*100)/2);
            yR2 = (yR2*100)-(max(yR2(yZ:end)*100)/2);
            yM  = 0;
            yM2 = nanmean(yR2(yZ:end));
            yS  = 5;
                        
            image2 = repmat(image1, [1, 1, 3]);
            
            cla(hAxis); hold(hAxis, 'on');
            imshow(image2, 'Parent', hAxis);
            truesize(hFig);
            
            lOp = {'Parent', hAxis, 'LineWidth', 1, 'linesmoothing','on'};
            
            yN = 1;
            x  = []; y = [];
            for m = 1:5:numel(xR)
              xv = [ 0 0]  + (xD+xR(m)*xF);
              if yN==1
                yv = [-35 35]  + yD + yM + yS;
                yN = 0;
              else
                yv = [-25 25]  + yD + yM + yS;
                yN = 1;
              end
              line(xv, yv, [0 0], 'Color', 'w', 'LineStyle', ':',   lOp{:}, 'LineWidth', 0.5);
            end
            
            plot(hAxis, xD+xR*xF, yD+yR2+yS, 'g', lOp{:}, 'Linewidth', 0.5);            
                        
            fQ = [obj.FundamentalFrequencies];
            
            if isnumeric(fQ) && ~isempty(fQ)
              for m = fQ
                yv = [-35 35]  + yD + yM + yS;
                xv = [0 0] + xD + m*xF;
                xv2 = [0 0] + xD*2;
                line(xv, yv, [0 0], 'Color', 'r', lOp{:}, 'LineWidth', 1);
                
                text(mean(xv2), max(yv)+1, 0, [num2str(m,'%3.1f')], 'Parent', hAxis, 'HorizontalAlignment', 'center', 'VerticalAlignment', 'top', 'Color', 'g', 'FontSize', 8, 'FontWeight', 'bold');
              end
%             end
%                        
%             if isnumeric(fQ) && ~isempty(fQ)
              fQ2 = fQ(1);
              for m = fQ2
                yv = [-20 20]  + yD + yM + yS; 
                xv = [0 0] + xD + m*xF;
                xv2 = [0 0] + xD*2;
                line(xv, yv, [0 0], 'Color', 'r', lOp{:}, 'LineWidth', 1);
                zi = [-1:1]+floor(m);
                zv = max(bFq(zi)); 
                zs = max(fqData(zi,5));
                %zs2 = max(fqData(zi,6));
                
                tVal = regexprep(num2str(zv,'%3.2e'),'([\d\.]+)(e[+-])[0]?(\d+)','$1$2$3');
                tStd = num2str(zs, '%1.2f');
                %tStd = [num2str(zs, '%1.2f') ' ' num2str(zs2, '%1.2f')];
                
                text(mean(xv2), min(yv)-15, 0, [tVal ' (' tStd ')'], 'Parent', hAxis, 'HorizontalAlignment', 'center', 'VerticalAlignment', 'bottom', 'Color', 'g', 'FontSize', 8, 'FontWeight', 'bold');

              end
            end
            
            DBG.dispdbg('Exporting Image...');
            
            img = export_fig(hFig, '-native', '-a2', ['-' renderer]);
            
            delete(hAxis);
            delete(hFig);
          else
            img = image1;
          end
          
        catch err
          debugStamp(err, 1);
          rethrow(err);
        end
        DBG.toc(R);
      end
      
      fxBusy=false;
    end

    
    function updateMetadata(obj)    
      
      obj.updateMetadata@Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      
      obj.fftImage  = [];
      obj.fftData   = [];
      
      width         = obj.Width;
      height        = obj.Height;
      
      if (width*height < 600^2)
        obj.resetRenderTimer;
      else
        debugStamp(sprintf('Not generating %d x %d', width, height), 5, obj);
      end      
      
    end
    
    
    function domain = get.Domain(obj)
      domain = obj.domain;
    end
    
    function set.Domain(obj, domain)
      if ~isequal(obj.domain, domain)
        obj.domain = domain;
      end
    end
    
    
  end
  
  methods(Access = protected)
    
    function resetRenderTimer(obj)
      try stop(obj.renderDelayTimer); end      
      if ~isscalar(obj.renderDelayTimer) || ~isa(obj.renderDelayTimer, 'timer') || ~isvalid(obj.renderDelayTimer)
        try delete(obj.renderDelayTimer); end
        obj.renderDelayTimer = GrasppeKit.DelayedCall(@(x,y) obj.renderDataPlot, 1.0, 'hold');
      end
      try start(obj.renderDelayTimer);  end
    end
    
  end
  
end

