#-------------------------------------------------------------------------------
#
# Chargement des donn�es et assignation des variables
#
#-------------------------------------------------------------------------------

data = read.csv(file.choose(), header=TRUE)
dataFlip = data[nrow(data):1,]  #Flip vertical car l'historique est invers�
NC = dataFlip[,2]
mWMC = dataFlip[,3]
mcBC = dataFlip[,4]
size = length(NC)
xAxis = 1:size

#-------------------------------------------------------------------------------
#
# Question 4 : Montrez visuellement l'�volution des m�triques NC, mWMC, et mcBC
#
#-------------------------------------------------------------------------------

#NC
plot(xAxis,NC,main = "�volution de NC dans le temps")

#mWMC
plot(xAxis,mWMC,main = "�volution de mWMC dans le temps")

#mcBC
plot(xAxis,mcBC,main = "�volution de mcBC dans le temps")

#-------------------------------------------------------------------------------
#
# Question 5 : Analyse des hypoth�ses
#
#-------------------------------------------------------------------------------

#Boxplot de NC
boxplot(NC, main = "NC",
        xlab = "Nombre de classes",
        col = "Orange",
        border = "Red",
        horizontal = TRUE,
        notch = TRUE)

#Boxplot de mWMC
boxplot(mWMC, main = "mWMC",
        xlab = "Moyenne de la m�trique WMC",
        col = "Orange",
        border = "Red",
        horizontal = TRUE,
        notch = TRUE,
	outline = TRUE)

#Boxplot de mcBC
boxplot(mcBC, main = "mcBC",
        xlab = "Moyenne de la m�trique classe_BC",
        col = "Orange",
        border = "Red",
        horizontal = TRUE,
        notch = TRUE,
 	outline = TRUE)

#Spearman pour NC et mWMC
cor.test(NC,mWMC,method = "spearman",exact=FALSE)

#Graphe de mWMC en fonction de NC
plot(NC,mWMC,main = "mWMC en fonction de NC")

#Spearman pour NC et mcBC
cor.test(NC,mcBC,method = "spearman",exact=FALSE)

#Graphe de mcBC en fonction de NC
plot(NC,mcBC,main = "mcBC en fonction de NC")


