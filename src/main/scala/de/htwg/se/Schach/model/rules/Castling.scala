package de.htwg.se.Schach.model.rules

import de.htwg.se.Schach.model._

object Castling {
  def doCastling(coordinates: Coordinates, newCoordinates: Coordinates, field: Field, king: King): Field = {
    val row = coordinates.row
    val col = coordinates.col
    val newRow = newCoordinates.row
    val newCol = newCoordinates.col
    val tmp = col - newCol
    if (tmp > 0) {
      return field.copy(field.cells.replaceCell(row, 0, Cell(field.cell(row, 0).colour, Option.empty)).replaceCell(row, 3, Cell(field.cell(row, 3).colour,
        Option.apply(field.cell(row, 0).contains.get.move))).replaceCell(row, col, Cell(field.cell(row, col).colour, Option.empty)).replaceCell(newRow, newCol,
        Cell(field.cell(newRow, newCol).colour, Option.apply(king.move))), None)
    } else {
      return field.copy(field.cells.replaceCell(row, 7, Cell(field.cell(row, 7).colour, Option.empty)).replaceCell(row, 5, Cell(field.cell(row, 5).colour,
        Option.apply(field.cell(row, 7).contains.get.move))).replaceCell(row, col, Cell(field.cell(row, col).colour, Option.empty)).replaceCell(newRow, newCol,
        Cell(field.cell(newRow, newCol).colour, Option.apply(king.move))), None)
    }
  }


  def castlingPossible(field: Field, coordinates: Coordinates, towerCol: Int, range: Range): Boolean = {
    val tmp = field.cell(coordinates.row, towerCol).contains
    (tmp.isDefined && (
      tmp.get match {
        case fig: Rook => fig.ability
        case _ => false
      }) && { // check if fields between rook and king are empty
      var u = true
      for (x <- range) {
        field.cell(coordinates.row, x).contains match {
          case Some(_) => u = false
          case _ =>
        }
      }
      u
    })
  }

  def castleKingside(field: Field, coordinates: Coordinates): Option[Coordinates] = {
    val towerCol = 7
    if (castlingPossible(field, coordinates, towerCol, (coordinates.col + 1) to (towerCol - 1))) Option.apply(Coordinates(coordinates.row, towerCol - 1)) else None

  }

  def castleQueenside(field: Field, coordinates: Coordinates): Option[Coordinates] = {
    val towerCol = 0
    if (castlingPossible(field, coordinates, towerCol, (towerCol + 1) to (coordinates.col - 1))) Option.apply(Coordinates(coordinates.row, 2)) else None
  }
}