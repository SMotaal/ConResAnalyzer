function img = bandPlotFFT(img, fftData, fQ)
  
  persistent hFig
  
  import(eval(NS.CLASS));
  
  dataColumn = 2;
  renderer = 'opengl';
  
  try
    
    image1    = img;
      
    width     = size(image1,2);
    height    = size(image1,1);
        
    hFig  = figure('Visible', 'off', 'Position', [-1000 -1000 width height], 'HandleVisibility','callback', 'Renderer', 'painters');
        
    hAxis = axes('Parent', hFig);
        
    % [bFq fftData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(image1b));
        
    bFq = fftData(:,dataColumn);
    
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
        zs = max(fftData(zi,5));
        %zs2 = max(fqData(zi,6));
        
        tVal = regexprep(num2str(zv,'%3.2e'),'([\d\.]+)(e[+-])[0]?(\d+)','$1$2$3');
        tStd = num2str(zs, '%1.2f');
        %tStd = [num2str(zs, '%1.2f') ' ' num2str(zs2, '%1.2f')];
        
        text(mean(xv2), min(yv)-15, 0, [tVal ' (' tStd ')'], 'Parent', hAxis, 'HorizontalAlignment', 'center', 'VerticalAlignment', 'bottom', 'Color', 'g', 'FontSize', 8, 'FontWeight', 'bold');
        
      end
    end
        
    img = export_fig(hFig, '-native', '-a2', ['-' renderer]);
        
    delete(hAxis);
    delete(hFig);
    
  catch err
    debugStamp(err, 1);
    rethrow(err);
  end
end
