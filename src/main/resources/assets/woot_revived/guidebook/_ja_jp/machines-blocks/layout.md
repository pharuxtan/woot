---
navigation:
    parent: machines-blocks/machines-blocks-index.md
    title: "レイアウト"
    icon: "woot_revived:layout"
---
# レイアウト

<BlockImage id="layout" scale="5"/>

<ItemImage id="layout" scale="0.5"/>レイアウトはファクトリーの設計図を表示します。

レイアウトを右クリックすることで、表示するのファクトリーのティアを変更できます。

プレビュー内のすべてのブロックは当たり判定が無く、破壊不可能であり、対応するブロックと直接置き換え可能です。

## クラフト

<RecipeFor id="layout" />

# プレビュー

<Row>
  <Column>
    ## 銅

    <GameScene zoom="2.5" interactive={true}>
        <ImportStructure src="../assets/layout/copper.snbt" />
        <IsometricCamera yaw="195" pitch="6" />
    </GameScene>
  </Column>

  <Column>
    ## 鉄

    <GameScene zoom="2.5" interactive={true}>
        <ImportStructure src="../assets/layout/iron.snbt" />
        <IsometricCamera yaw="195" pitch="6" />
    </GameScene>
  </Column>

  <Column>
    ## 金

    <GameScene zoom="2.5" interactive={true}>
        <ImportStructure src="../assets/layout/gold.snbt" />
        <IsometricCamera yaw="195" pitch="6" />
    </GameScene>
  </Column>

  <Column>
    ## ダイヤモンド

    <GameScene zoom="2.5" interactive={true}>
        <ImportStructure src="../assets/layout/diamond.snbt" />
        <IsometricCamera yaw="195" pitch="6" />
    </GameScene>
  </Column>

  <Column>
    ## ネザライト

    <GameScene zoom="2.5" interactive={true}>
        <ImportStructure src="../assets/layout/netherite.snbt" />
        <IsometricCamera yaw="195" pitch="6" />
    </GameScene>
  </Column>
</Row>
