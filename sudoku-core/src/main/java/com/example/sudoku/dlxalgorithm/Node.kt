package com.example.sudoku.dlxalgorithm

data class Node(
    var columnHeader: Node? = null,
    var rowId: Int? = null,
    var name: String? = null, // Used for prints
) {
    var right: Node = this
    var up: Node = this
    var down: Node = this
    var left: Node = this

    var numOfNodes = 0

    // Used for inserting column headers
    fun insertRight(node: Node) {
        // Pointers of new node
        node.left = this
        node.right = this.right

        // Pointers of node that was to the right before insert
        this.right.left = node

        // Update this node to point to new node
        this.right = node
    }

    // Used for inserting matrix nodes
    fun insertDown(node: Node) {
        // Set pointers of new node
        node.up = this
        node.down = this.down

        // Pointers of node that was below this node before insert
        this.down.up = node

        // Update this node
        this.down = node
    }

    // Removes column from the matrix, as well as every row that has node in said column
    fun cover() {
        // Hides column by pointing this node's neighbors at each other
        right.left = this.left
        left.right = this.right

        // Going top to bottom
        var colNode = this.down // node in column
        while (colNode != this) {
            // Going left to right
            var rowNode = colNode.right // node in row
            if (colNode != this) {
                while (rowNode != colNode) {
                    // Hides node by pointing (up, down) neighbors at each other
                    rowNode.up.down = rowNode.down
                    rowNode.down.up = rowNode.up

                    colNode.columnHeader!!.numOfNodes--
                    // Move right
                    rowNode = rowNode.right
                }
                // Move down
                colNode = colNode.down
            }
        }
    }

    // Reinserts column to the matrix as well as every covered node, by doing cover function backwards
    fun uncover() {
        // Going bottom to top
        var colNode = this.up
        while (colNode != this) {
            // Going left to right
            var rowNode = colNode.left
            while (rowNode != colNode) {
                colNode.columnHeader!!.numOfNodes++

                rowNode.up.down = rowNode
                rowNode.down.up = rowNode

                // Move left
                rowNode = rowNode.left
            }
            // Move up
            colNode = colNode.up
        }

        // Change this node's neighbors points back to this node
        this.right.left = this
        this.left.right = this
    }
}

// Converts exact cover boolean matrix into dancing links matrix
fun Array<BooleanArray>.toNode(): Node = Node(name = "Root node").apply {
    val columnHeaders = ArrayList<Node>()
    val numOfColumns = when {
        this@toNode.isNotEmpty() -> this@toNode[0].size
        else -> 0
    }

    // Convert headers
    for (i in 0..<numOfColumns) {
        columnHeaders.add(Node(name = "H$i"))
        //Insert new columns to the right
        if (i > 0) columnHeaders[i - 1].insertRight(columnHeaders[i])
        else {
            this.right.insertRight(columnHeaders[0])
        }
    }

    // For each row in matrix
    forEachIndexed { rowIndex, row ->
        // For each column in matrix
        var prevNode: Node? = null
        row.forEachIndexed { colIndex, conditionIsSet ->
            if (conditionIsSet) {
                val header = columnHeaders[colIndex]
                val newNode = Node(name = "r${rowIndex}c$colIndex", columnHeader = header, rowId = rowIndex)

                // Loop is going top to bottom, so inserting below node that is above header(bottom of column)
                // will always insert at the bottom of the column
                header.up.insertDown(newNode)

                // Loop is going left to right, so inserting will always be to the right of the previous node
                prevNode?.insertRight(newNode)
                prevNode = newNode
            }
        }
    }
}

// Searches for column with min number of nodes
fun Node.findBestColumn(): Node? {
    var header: Node? = null
    var minNodes = Int.MAX_VALUE
    var next = right
    while(next != this && minNodes > 1){
        if(next.numOfNodes < minNodes){
            minNodes = next.numOfNodes
            header = next
        }
        next = next.right
    }
    return header
}