all:
	pdflatex Facharbeit
	bibtex Facharbeit
	pdflatex Facharbeit
	pdflatex Facharbeit

clean:
	rm Facharbeit.aux
	rm Facharbeit.log
	rm Facharbeit.bbl
	rm Facharbeit.blg
	rm Facharbeit.toc
	:all
