try
  if ~(exist('patchGenerator', 'var') || ~isscalar(patchGenerator) ...
      && isa(patchGenerator, 'Grasppe.ConRes.PatchGenerator'))
    conreslab;
  end
  
  r = Grasppe.Kit.ConRes.ResolutionRange,
  c = Grasppe.Kit.ConRes.ContrastRange,
  
  for t = 5:5:95  %Grasppe.Kit.ConRes.ToneRange,
    R=tic;
    patchGenerator.Processor.RunSeries(t, c, r);
    toc(R);
  end
end
