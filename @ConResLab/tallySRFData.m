function [ output_args ] = tallySRFData(data) % SRF, Series )
  %TALLYSRFDATA Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  import Grasppe.ConRes.Math;
  
  forceRender = true;
  forceOutput = false;
  
  if ~exist('data', 'var')
    data                      = PatchSeriesProcessor.LoadData();
    data.SRF                  = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
    data.PRF                  = PatchSeriesProcessor.LoadData('PRF', 'PRFData');
  end
  
  parameters                = data.Series.Parameters;
  
  fieldTable                = data.Fields.Table;
  seriesRows                = data.Grids.Halftone.Rows;
  seriesRange               = 1:seriesRows;
  
  htRows                    = data.Grids.Halftone.Rows;
  scRows                    = data.Grids.Screen.Rows;
  ctRows                    = data.Grids.Contone.Rows;
  mtRows                    = data.Grids.Monotone.Rows;
  
  scIdxs                    = data.Grids.Screen.Index;
  ctIdxs                    = data.Grids.Contone.Index;
  mtIdxs                    = data.Grids.Monotone.Index;
  
  scRefs                    = data.Grids.Screen.Reference;
  ctRefs                    = data.Grids.Contone.Reference;
  mtRefs                    = data.Grids.Monotone.Reference;
  
  seriesTable               = data.Series.Table;
  seriesParameters          = data.Series.Parameters;
  seriesVariables           = data.Series.Variables;
  
  htPaths                   = data.Series.Paths.Halftone;
  scPaths                   = data.Series.Paths.Screen;
  ctPaths                   = data.Series.Paths.Contone;
  mtPaths                   = data.Series.Paths.Monotone;
  
  htSRFs                    = data.SRF.Halftone;
  scSRFs                    = data.SRF.Screen;
  ctSRFs                    = data.SRF.Contone;
  mtSRFs                    = data.SRF.Monotone;
  
  htPRFs                    = data.PRF.Monotone.Halftone;
  scPRFs                    = data.PRF.Monotone.Screen;
  ctPRFs                    = data.PRF.Monotone.Contone;
  mtPRFs                    = data.PRF.Monotone.Monotone;
    
  %% Data Columns
  % [B isum S imean istd mstd];
  bandWidth   = 1;
  bandSum     = 2;
  filterSum   = 3;
  bandMean    = 4;
  bandSigma   = 5;
  
  %% figure Columns
  plotColumn              = bandMean;
  labelColumns            = [bandMean, bandSigma];
  
  sumTable                = zeros(seriesRows, 4 + 4 + 4 + 4 + 4);
  %outputTable             = cell(seriesRows, 5); % 4 + size(sumTable,2));
  outputText              = cell(seriesRows,1);
  
  for m = seriesRange; %fliplr(seriesRange(1:13:end)) %(1:25:end)
    
    try
    
    if rem(m, 50)==0,
      dispf('Generating Series Plots... %d of %d', m, seriesRows);
    end
    
    %% Output Checks (All True!)
    
    %% Row Indices
    htIdx                   = m;
    scIdx                   = scRefs(m);
    ctIdx                   = ctRefs(m);
    mtIdx                   = mtRefs(m);
    
    %% Resource IDs
    htID                    = seriesTable{m, 4};
    scID                    = seriesTable{m, 3};
    ctID                    = seriesTable{m, 5};
    mtID                    = seriesTable{m, 6};
    
    %% Spatial Radial Frequency Data
    htSRF                   = htSRFs{htIdx, 2};
    scSRF                   = scSRFs{scIdx, 2};
    ctSRF                   = ctSRFs{ctIdx, 2};
    mtSRF                   = mtSRFs{mtIdx, 2};
    
    %% Cross-Product Spatial Radial Frequency Data
    htPRF                   = htPRFs{htIdx, 2};
    scPRF                   = scPRFs{scIdx, 2};
    ctPRF                   = ctPRFs{ctIdx, 2};
    mtPRF                   = mtPRFs{mtIdx, 2};
    
    %% Patch Parameters
    mRTV                    = parameters(m).Patch.Mean;
    mCON                    = parameters(m).Patch.Contrast;
    mRES                    = parameters(m).Patch.Resolution;
    
    mSize                   = parameters(m).Patch.Size;
    mDPI                    = parameters(m).Scan.Resolution;
    mScale                  = parameters(m).Scan.Scale/100;
    mPPI                    = mDPI*mScale;
    
    %% Patch Variables
    mPixels                 = mDPI*mScale/25.4; %
    mfR                     = Math.VisualResolution(mPPI) * 7;
    mfQ                     = mRES/mSize * mPixels; %mRES/mPixels * mSize/mPixels * 2;
    [mBP mBW]               = Math.FrequencyRange(mSize, mPPI);
    
    %% Fundamental Data Row
    fQRows                  = min(max(1, round(mfQ) + [1:10]), size(mtSRF, 1));
    [fQMax fQRow]           = max(mtSRF(fQRows, bandMean));
    fQRow                   = round(mfQ) + fQRow-1;
    
    [mCMPPath mCMPExists]   = PatchSeriesProcessor.GetResourcePath('Patch CompositeImage', htID, 'png');
    [mSRFPath mSRFExists]   = PatchSeriesProcessor.GetResourcePath('Patch SRFPlot', htID, 'png');
    [mPRFPath mPRFExists] 	= PatchSeriesProcessor.GetResourcePath('Patch PRFPlot', htID, 'png');
    
    if forceRender || ~(mCMPExists && mSRFExists && mPRFExists)
      %% Composite Images
      [mComp mW mH]         = composePatchImage(htID, scID, ctID, mtID);
            
      %% Composite Plots
      [mSRFPlots]           = composePatchPlots(mSRFPath, mfQ, fQRow, plotColumn, labelColumns, mW, mH, scSRF, htSRF, ctSRF, mtSRF);
      [mPRFPlots]           = composePatchPlots(mPRFPath, mfQ, fQRow, plotColumn, labelColumns, mW, mH, scPRF, htPRF, ctPRF, mtPRF);
      
      %% Save Images
      % PatchSeriesProcessor.SaveImage(mComp, 'Patch CompositeImage', htID);
      PatchSeriesProcessor.SaveImage(mSRFPlots, 'Patch SRFPlot', htID);
      PatchSeriesProcessor.SaveImage(mPRFPlots, 'Patch PRFPlot', htID);
      
    end
    
    l1 = labelColumns(1);
    l2 = labelColumns(2);
    
    sumTable(m, :)          = [ ...
      mRTV mCON mRES mfQ ...
      scSRF(fQRow, l1) htSRF(fQRow, l1) ctSRF(fQRow, l1) mtSRF(fQRow, l1) ...
      scSRF(fQRow, l2) htSRF(fQRow, l2) ctSRF(fQRow, l2) mtSRF(fQRow, l2) ...
      scPRF(fQRow, l1) htPRF(fQRow, l1) ctPRF(fQRow, l1) mtPRF(fQRow, l1) ...
      scPRF(fQRow, l2) htPRF(fQRow, l2) ctPRF(fQRow, l2) mtPRF(fQRow, l2) ...
      ];
    %     end
    
    %outputTable(m,:)        = [m, mCMPPath, mSRFPath, mPRFPath, num2cell(sumTable(m,:))];
    
    %outputTable(m,1:4)        = {m, mCMPPath, mSRFPath, mPRFPath};

    outputText{m}            = strtrim(sprintf([ ...
      '%d'    '\t'  '%s'    '\t'  '%s'    '\t'  '%s'    '\t' ...
      '%1.1f' '\t'  '%1.1f' '\t'  '%1.3f' '\t'  '%1.1f' '\t' ...
      '%1.1f' '\t'  '%1.1f' '\t'  '%1.1f' '\t'  '%1.1f' '\t' ...
      '%1.1f' '\t'  '%1.1f' '\t'  '%1.1f' '\t'  '%1.1f' '\t'], ...
      m, mCMPPath, mSRFPath, mPRFPath, sumTable(m,:)));
    
    catch err
      debugStamp();
      beep;
    end
    
  end
  
  [sumPath sumExists]       = PatchSeriesProcessor.GetResourcePath('output', 'SummaryData', 'csv');

  
  % dlmwrite(sumPath, sumTable);
  
    [outPath outExists]       = PatchSeriesProcessor.GetResourcePath('output', 'SummaryTable', 'csv');
%   outHeading =  {'#', 'Composite', 'SRF', 'Cross-SRF', strtrim(sprintf('%s\t', ...
%     'RTV', 'CON', 'RES', 'FQ',...
%     'HT-Avg', 'SC-Avg', 'CT-Avg', 'MT-Avg', ...
%     'HT-Std', 'SC-Std', 'CT-Std', 'MT-Std', ...
%     'M.HT-Avg', 'M.SC-Avg', 'M.CT-Avg', 'M.MT-Avg', ...
%     'M.HT-Std', 'M.SC-Std', 'M.CT-Std', 'M.MT-Std'))}

  outHeading =  {strtrim(sprintf('%s\t', ...
    '#', 'Composite', 'SRF', 'Cross-SRF', ...
    'RTV', 'CON', 'RES', 'FQ',...
    'HT-Avg', 'SC-Avg', 'CT-Avg', 'MT-Avg', ...
    'HT-Std', 'SC-Std', 'CT-Std', 'MT-Std', ...
    'M.HT-Avg', 'M.SC-Avg', 'M.CT-Avg', 'M.MT-Avg', ...
    'M.HT-Std', 'M.SC-Std', 'M.CT-Std', 'M.MT-Std'))};


  
  %outputText = vertcat( outHeading, outputText);
  
  % cell2csv(outPath, vertcat( outHeading, outputText), '\n');
  
   
%   outputText               = mat2clip(vertcat({'#', 'Composite', 'SRF', 'Cross-SRF', ...
%     'RTV', 'CON', 'RES', 'FQ',...
%     'HT-Avg', 'SC-Avg', 'CT-Avg', 'MT-Avg', ...
%     'HT-Std', 'SC-Std', 'CT-Std', 'MT-Std', ...
%     'M.HT-Avg', 'M.SC-Avg', 'M.CT-Avg', 'M.MT-Avg', ...
%     'M.HT-Std', 'M.SC-Std', 'M.CT-Std', 'M.MT-Std', ...
%     }, outputTable));
  
  
  
  
end

function [patchPlot    ] = composePatchPlots(pth, fQ, F, P, L, W, H, varargin)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  persistent hFig
  
  %% Create Figure (or Reset)
  
  w = 600;
  h = H*w/W;
  
  R = 1.5;
  
  
  if isempty(hFig) || ~ishandle(hFig)
    hFig  = figure('Visible', 'off', 'Position', [1 1 w h], 'Renderer', 'opengl'); %, 'color', 'none'); % 'HandleVisibility','callback', 
  else
    clf(hFig, 'reset');
    set(hFig, 'Visible', 'off', 'Position', [1 1 w h]);
  end
  
  axOpts  = {'Parent', hFig, 'color', 'none', 'Units', 'normalized', 'Position', [0 0 1 1], 'PlotBoxAspectRatio', [w h 1], 'Visible', 'off', 'clipping', 'on'};
  %'box', 'on', 
  hAxes2  = axes(axOpts{:});
  hAxes   = axes('YScale', 'log', axOpts{:});
  
  % scSRF, htSRF, ctSRF, mtSRF
  %colors  = {'c', 'g', [1 0.75 0] , 'r', 'k'}; %
  colors  = {[0 1 1], [0 1 0], [1 0.75 0], [1 0 0], [0 0 0]};
  opts    = {'LineWidth', R*0.85}; %, 'linesmoothing','on'};
  
  data    = varargin;
  D       = 2-1;
  E       = 0;
  F       = F-D;
  
  %% Process Data
  for n = 1:numel(data)
    E           = max(E, size(data{n}, 1));
    data{n}     = data{n}(D+1:end,:);
    vMin(n)     = nanmin(data{n}(:,P));
    vMax(n)     = nanmax(data{n}(:,P));
    vMean(n)    = nanmean(data{n}(:,P));
  end
  
  fMin          = min(vMin);
  fMax          = max(vMax);
  fMean         = mean(vMean);
  
  hold(hAxes,   'on');
  hold(hAxes2,  'on');
  
  dMin1         = [];
  dMax1         = [];
  dMin2         = [];
  dMax2         = [];
  
  %% Plot Curves
  for n = [1 3:numel(data)]
    DP2 = data{n}(:,L(2));
    
    hP2 = plot(DP2, 'color', colors{n}, 'Parent', hAxes2, opts{:}, 'linestyle', ':'); % , 'linewidth', R/3);
    
    dMin2 = min([dMin2 DP2(:)']);
    dMax2 = max([dMax2 DP2(:)']);
  end  
  
  DP2 = data{2}(:,L(2));
  hP2 = plot(DP2, 'color', colors{2}, 'Parent', hAxes2, opts{:}, 'linestyle', ':'); %, 'linewidth' , R);

  DF1 = 0; %data{1}(:,L(1))-max(data{1}(:,L(1)));
  
  %% Plot Curves
  for n = [1 3:numel(data)]
    DP1 = data{n}(:,L(1))-DF1;
    hP1 = plot(DP1, 'color', colors{n}, 'Parent', hAxes, opts{:}); %, 'linewidth', R/3);
    
    dMin1 = min([dMin1 DP1(:)']);
    dMax1 = max([dMax1 DP1(:)']);
  end
  
  DP1 = data{2}(:,L(1))-DF1;
  hP1 = plot(DP1, 'color', colors{2}, 'Parent', hAxes, opts{:}); %, 'linewidth' , R);
  

  

  %% Fundamental Data
  vData   = [];
  for n = 1:numel(data)
    vData = [vData reshape(data{n}(  min(F, size(data{n},1)  ), L),[],1)];
  end
  
  [fR fE] = sciparts(reshape(vData, [], 1));
  
  fR      = reshape(fR, size(vData));
  fE      = reshape(fE, size(vData));
  
  %% Prepare formatter
  fNumF = '%1.2f\t';
  fStrF = '';
  for n = 1:size(fR,2)
    fStrF = [fStrF '\\color[rgb]{' num2str(colors{n}, ' %1.2f ') '}' fNumF];
  end
  
  %% Prepare Text
  fStr = {num2str(fQ,'%1.2f\n')};
  for n = 1:size(fR,1)
    %vStr      = num2str(fR(n, :),'%1.2f\t');
    vStr      = num2str(fR(n, :), fStrF);
    vStr      = [vStr sprintf('\te%+1d', fE(n,1))];
    fStr(n+1) = {vStr};
  end
  
  %fStr        = strcat(fStr,sprintf('\t'));
  
  %fStr        = [fStr, ' ']; %  (fStr, '')%vertcat(fStr, {''});
  
  yl = get(hAxes, 'ylim'); % [fMin fMax]
  xl = get(hAxes, 'xlim');
  
    
  %% Fundamental Line %round(fQ)-D %round(fQ)-D
  line([F F]+1, yl, [0 0]-50, 'Color', [0.8 0 0], 'Parent', hAxes, opts{:}, 'LineWidth', R*2);  
  
  %fQ-D+20
  
  text(max(xl)*0.95, max(yl)*0.95, 0, fStr, 'Parent', hAxes, 'HorizontalAlignment', 'right', ...
    'VerticalAlignment', 'top', 'Color', 'g', 'FontSize', 8*R, 'FontName', 'DIN-Bold', ... 
    'Interpreter', 'tex'); % 'FontWeight', 'bold',
  
  %'Units', 'normalized'

  %tStd = num2str(zs, '%1.2f');
  
  set(hAxes, 'XTickLabel', [], 'YTickLabel', [], 'ZTickLabel', [], 'XTick', [], 'YTick', [], 'ZTick', []);
  set(hAxes2, 'XTickLabel', [], 'YTickLabel', [], 'ZTickLabel', [], 'XTick', [], 'YTick', [], 'ZTick', []);
  %set(hAxes,  'XLim', [D-1 E-D+1], 'YLim', [dMin1 dMax1]);
  %set(hAxes2,  'XLim', [D-1 E-D+1], 'YLim', [dMin2 dMax2]);
  
  %img = imresize(print2array(hFig,2), 0.5);
  
  if ischar(pth) && ~isempty(pth)
    %im2frame
    
    %imwrite(img, pth);
    
    export_fig(pth, '-nocrop', '-a4', '-zbuffer', '-transparent', hFig); %'-native'
    patchPlot = imread(pth);
  else
    patchPlot = export_fig('nocrop', '-a2', '-opengl', '-transparent', hFig); %  '-native',
  end
    
  
  %delete(hAxes);
  
  
end

function [patchComp W H] = composePatchImage(htID, scID, ctID, mtID)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  patchTypes              = {'Screen', 'Halftone', 'Contone', 'Monotone'};
  patchIDs                = {scID, htID, ctID, mtID};
  imageGroups             = {
    'Image', 'RetinaImage';
    'FFTImage', 'RetinaFFTImage'};
  
  compDim                 = 1;
  
  imageType               = cell(1, 2);
  img                     = cell(numel(patchTypes), numel(imageGroups), 2);
  
  imgSizes                = [];
  
  N                       = numel(patchTypes);
  P                       = size(imageGroups,2);
  Q                       = size(imageGroups,1);
  
  %% Load All Images
  for n = 1:N
    for p = 1:P
      for q = 1:Q
        img{n, p, q}      = PatchSeriesProcessor.LoadImage([patchTypes{n} ' ' imageGroups{p, q}], patchIDs{n});
        r                 = sub2ind([N P Q], n, p, q);
        imgSizes(r,:)     = size(img{n, p, q});
      end
    end
  end
  
  %% Crop All Images
  
  H                       = min(imgSizes(:,1));
  W                       = min(imgSizes(:,2));
  
  X1                      = 1+floor((imgSizes(:,2) - W) / 2);
  X2                      = X1+W-1;
  
  Y1                      = 1+floor((imgSizes(:,1) - H) / 2);
  Y2                      = Y1+H-1;
  
  for n = 1:N
    for p = 1:P
      for q = 1:Q
        r                 = sub2ind([N P Q], n, p, q);
        img{n, p, q}      = img{n, p, q}(Y1(r):Y2(r), X1(r):X2(r));
      end
    end
  end
  
  %% Composite Patch Image
  patchComp               = [];
  
  for n = 1:N
    groupComp             = [];
    for p = 1:P
      imageComp           = img{n, p, 1};
      S                   = size(imageComp, compDim);
      M                   = round(S/2):S;
      if compDim==2
        imageComp(:,M)    = img{n, p, 2}(:,M);
      else
        imageComp(M,:)    = img{n, p, 2}(M,:);
      end
      groupComp           = vertcat(groupComp, imageComp);
    end
    patchComp             = horzcat(patchComp, groupComp);
  end
  
  W = W * N;
  H = H * P;
  
end
