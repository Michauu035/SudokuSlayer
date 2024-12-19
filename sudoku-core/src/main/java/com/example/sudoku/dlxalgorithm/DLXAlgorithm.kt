package com.example.sudoku.dlxalgorithm

import java.util.Stack

data class AlgorithmResult(
	val solution: MutableList<Int> = mutableListOf(),
	val solutions: MutableList<MutableList<Int>> = mutableListOf(),
)

fun findSolution(rootNode: Node, limit: Int = 0): AlgorithmResult {
	val result = AlgorithmResult()
	val solution = Stack<Int>()
	val solutions = ArrayList<MutableList<Int>>()
	fun search() {
		var columnHeader = rootNode.findBestColumn()
		if (columnHeader == null) {
			solutions.add(solution.toMutableList())
			result.solution += solution
			result.solutions.addAll(solutions)
		}
		var node = columnHeader?.down
		while (node != columnHeader) {
			solution.push(node?.rowId)
			var rightNode = node
			do {
				rightNode?.columnHeader?.cover()
				rightNode = rightNode?.right
			} while (rightNode != node)

			search()

			solution.pop()
			var startNode = node?.left
			var leftNode = startNode
			do {
				leftNode?.columnHeader?.uncover()
				leftNode = leftNode?.left
			} while (leftNode != startNode)

			node = node?.down

			if (limit > 0 && solutions.size >= limit) break
		}
	}
	search()
	return AlgorithmResult(
		solution = solution,
		solutions = solutions
	)
}