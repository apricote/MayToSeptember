F = Facharbeit

all:
	pdflatex $(F)
	biber $(F)
	pdflatex $(F)
	pdflatex $(F)

clean:
	rm -f $(F).bbl $(F).blg $(F).pdf $(F).dvi $(F).aux $(F).bcf $(F).log $(F).run.xml $(F).toc
