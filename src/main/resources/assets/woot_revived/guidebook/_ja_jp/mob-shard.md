---
navigation:
  title: "モブを記録するには?"
  position: 20
  icon: "mob_shard"
---
# モブを記録するには?

モブを記録するには<ItemImage id="mob_shard" scale="0.5"/>モブシャードを使用します。

## モブシャードの作り方

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="assets/anvil/mob_shard.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="mob_shard" scale="2"/>
  これを作るには<ItemImage id="shard_mold" scale="0.5"/>欠片の金型と2つの<ItemImage id="stygian_ingot" scale="0.5"/>スティジアンインゴットが必要になります
</Row>

## モブシャードの使い方

モブを記録するためのモブシャードを入手したら、まずシャードでモブを攻撃する必要があります

シャードをモブに投げつけることも可能です。投擲したシャードは、
溶岩以外で破壊されることはありません。

モブを攻撃した後もモブシャードが記録状態になっていない場合、そのモブはブラックリストに登録されているか、生きているエンティティではない可能性があります。

モブを記録に成功したら、モブシャードをインベントリに保持した状態で、任意の道具を使用し記録したモブを5回倒しましょう

5体の敵を倒すと、モブシャードは完全にプログラムされ、<ItemImage id="fake_spawner" scale="0.5"/>疑似スポナーを作成できるようになります。

## 疑似スポナーの作り方
ファクトリーに記録した個体を認識させるには、<ItemImage id="mob_shard" scale="0.5"/>モブシャードで<ItemImage id="fake_spawner" scale="0.5"/>疑似スポナーを作成する必要があります

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="assets/anvil/fake_spawner.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="fake_spawner" scale="2"/>
  これを作るには<ItemImage id="mob_shard" scale="0.5"/>モブシャード、<ItemImage id="prism" scale="0.5"/>プリズム、<ItemImage id="factory_base" scale="0.5"/>ファクトリーベースが必要です
  </Row>