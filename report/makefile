# Mainfile:
m=master
pdf:
	pdflatex -interaction=nonstopmode -halt-on-error $m.tex
clean:
	rm -f *.aux *.dvi *.lof *.log *.lol *.lot *.out *.ps *.toc *.bbl *.blg *.lox
release:
	make clean
	make pdf
	bibtex master.aux
	make pdf
	make pdf
