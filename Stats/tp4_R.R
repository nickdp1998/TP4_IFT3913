#-------------------------------------------------------------------------------
#
# Chargement des données et assignation des variables
#
#-------------------------------------------------------------------------------

data = read.csv(file.choose(), header=TRUE)
dataFlip = data[nrow(data):1,]  #Flip vertical car l'historique est inversé
NC = dataFlip[,2]
mWMC = dataFlip[,3]
mcBC = dataFlip[,4]
size = length(NC)
xAxis = 1:size

#-------------------------------------------------------------------------------
#
# Question 4 : Montrez visuellement l'évolution des métriques NC, mWMC, et mcBC
#
#-------------------------------------------------------------------------------

#NC
plot(xAxis,NC,main = "Évolution de NC dans le temps")

#mWMC
plot(xAxis,mWMC,main = "Évolution de mWMC dans le temps")

#mcBC
plot(xAxis,mcBC,main = "Évolution de mcBC dans le temps")

#-------------------------------------------------------------------------------
#
# Peut-être utile pour la question 5
#
#-------------------------------------------------------------------------------

# #Boxplot de NC
# boxplot(NC, main = "NC",
#         xlab = "Nombre de classes",
#         col = "Orange",
#         border = "Red",
#         horizontal = TRUE,
#         notch = TRUE)
# text(x = boxplot.stats(NC)$stats, labels = boxplot.stats(NC)$stats,
#      y = 1.25)
# 
# #Boxplot de mWMC
# boxplot(mWMC, main = "mWMC",
#         xlab = "Moyenne de la métrique WMC",
#         col = "Orange",
#         border = "Red",
#         horizontal = TRUE,
#         notch = TRUE,
# 	outline = FALSE)
# text(x = round(boxplot.stats(mWMC)$stats, digits = 3), labels = round(boxplot.stats(mWMC)$stats, digits = 3),
#      y = 1.25)
# 
# #Boxplot de mcBC
# boxplot(mcBC, main = "mcBC",
#         xlab = "Moyenne de la métrique classe_BC",
#         col = "Orange",
#         border = "Red",
#         horizontal = TRUE,
#         notch = TRUE,
#  	outline = FALSE)
# text(x = round(boxplot.stats(mcBC)$stats, digits = 3), labels = round(boxplot.stats(mcBC)$stats, digits = 3),
#      y = 1.25)

