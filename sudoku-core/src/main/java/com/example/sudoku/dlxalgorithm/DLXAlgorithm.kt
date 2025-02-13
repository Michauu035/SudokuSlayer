package com.example.sudoku.dlxalgorithm

import com.example.sudoku.dlxalgorithm.model.DLXNode
import com.example.sudoku.dlxalgorithm.model.DataNode
import com.example.sudoku.dlxalgorithm.model.HeaderNode
import com.example.sudoku.dlxalgorithm.model.RootNode
import com.example.sudoku.dlxalgorithm.model.findBestColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import java.util.Stack

object DLXAlgorithm {
	fun RootNode.solve(collect: (ArrayList<Int>) -> Boolean) {
		solveProblem(collect = collect)
	}

	fun RootNode.solveAll(): Collection<List<Int>> =
		ArrayList<List<Int>>().apply {
			solveProblem { add(it) }
		}

	@OptIn(ExperimentalCoroutinesApi::class)
	fun CoroutineScope.solveSuspend(rootNode: RootNode) = produce<List<Int>>(capacity = 2) {
		rootNode.solveAll().forEach {
			channel.send(it)
		}
	}

	private fun RootNode.solveProblem(
		solution: Stack<Int> = Stack<Int>(),
		collect: (ArrayList<Int>) -> Boolean
	) {
		val header: HeaderNode? = findBestColumn()
		when(header) {
			null -> {
				collect(ArrayList(solution))
			}
			else -> {
				var node = header.down
				while (node != header as DLXNode) {
					solution.push(( node as DataNode ).rowId)
					var rightNode = node
					do {
						( rightNode as DataNode).header.cover()
						rightNode = rightNode.right
					} while (rightNode != node)

					solveProblem(solution, collect)

					solution.pop()
					var startNode = node.left
					var leftNode = startNode
					do {
						( leftNode as DataNode ).header.uncover()
						leftNode = leftNode.left
					} while (leftNode != startNode)

					node = node.down
				}
			}
		}
	}
}
