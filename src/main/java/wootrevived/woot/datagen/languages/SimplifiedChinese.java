package wootrevived.woot.datagen.languages;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.upgrades.*;

public class SimplifiedChinese extends LanguageProvider {
    public SimplifiedChinese(PackOutput output){
        super(output, Woot.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.woot_revived", "Woot：重生");
        add("config.jade.plugin_woot_revived.machines", "Woot机械插件");

        add(BlocksRegistry.STYGIAN_ANVIL_BLOCK.get(), "幽冥砧");
        add(ItemsRegistry.STYGIAN_HAMMER_ITEM.get(), "幽冥锤");
        add(ItemsRegistry.PLATE_MOLD_ITEM.get(), "板模具");
        add(ItemsRegistry.SHARD_MOLD_ITEM.get(), "碎片模具");
        add(ItemsRegistry.DYE_CASING_MOLD_ITEM.get(), "染料框架模具");

        add(BlocksRegistry.CREATIVE_POWER_BLOCK.get(), "创造能源");
        add(BlocksRegistry.CREATIVE_TANK_BLOCK.get(), "创造储罐");

        add(BlocksRegistry.FACTORY_BASE_BLOCK.get(), "工厂基座");

        add(BlocksRegistry.HEART_BLOCK.get(), "工厂核心");
        add(BlocksRegistry.FAKE_SPAWNER_BLOCK.get(), "伪刷怪笼");

        add(BlocksRegistry.COPPER_CELL_BLOCK.get(), "铜生命单元");
        add(BlocksRegistry.COPPER_PYLON_BLOCK.get(), "铜塔柱");
        add(BlocksRegistry.COPPER_PLINTH_BLOCK.get(), "铜柱基");
        add(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get(), "附魔铜板");
        add(ItemsRegistry.COPPER_SHARD_ITEM.get(), "铜碎片");

        add(BlocksRegistry.IRON_CELL_BLOCK.get(), "铁生命单元");
        add(BlocksRegistry.IRON_PYLON_BLOCK.get(), "铁塔柱");
        add(BlocksRegistry.IRON_PLINTH_BLOCK.get(), "铁柱基");
        add(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get(), "附魔铁板");
        add(ItemsRegistry.IRON_SHARD_ITEM.get(), "铁碎片");

        add(BlocksRegistry.GOLD_CELL_BLOCK.get(), "金生命单元");
        add(BlocksRegistry.GOLD_PYLON_BLOCK.get(), "金塔柱");
        add(BlocksRegistry.GOLD_PLINTH_BLOCK.get(), "金柱基");
        add(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get(), "附魔金板");
        add(ItemsRegistry.GOLD_SHARD_ITEM.get(), "金碎片");

        add(BlocksRegistry.DIAMOND_CELL_BLOCK.get(), "钻石生命单元");
        add(BlocksRegistry.DIAMOND_PYLON_BLOCK.get(), "钻石塔柱");
        add(BlocksRegistry.DIAMOND_PLINTH_BLOCK.get(), "钻石柱基");
        add(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get(), "附魔钻石板");
        add(ItemsRegistry.DIAMOND_SHARD_ITEM.get(), "钻石碎片");

        add(BlocksRegistry.NETHERITE_CELL_BLOCK.get(), "下界合金生命单元");
        add(BlocksRegistry.NETHERITE_PYLON_BLOCK.get(), "下界合金塔柱");
        add(BlocksRegistry.NETHERITE_PLINTH_BLOCK.get(), "下界合金柱基");
        add(ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM.get(), "附魔下界合金板");
        add(ItemsRegistry.NETHERITE_SHARD_ITEM.get(), "下界合金碎片");

        add(BlocksRegistry.FACTORY_CONNECT_BLOCK.get(), "工厂连接器");
        add(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK.get(), "主基座");
        add(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK.get(), "副基座");

        add(BlocksRegistry.IMPORT_BLOCK.get(), "原料输入口");
        add(BlocksRegistry.EXPORT_BLOCK.get(), "战利品输出口");

        add(BlocksRegistry.FACTORY_UPGRADE_BLOCK.get(), "升级插槽");

        add(Efficiency.COPPER_EFFICIENCY_ITEM.get(), "效率升级 I");
        add(Efficiency.IRON_EFFICIENCY_ITEM.get(), "效率升级 II");
        add(Efficiency.GOLD_EFFICIENCY_ITEM.get(), "效率升级 III");
        add(Efficiency.DIAMOND_EFFICIENCY_ITEM.get(), "效率升级 IV");
        add(Efficiency.NETHERITE_EFFICIENCY_ITEM.get(), "效率升级 V");
        add("info.woot_revived.upgrade.efficiency.desc.0", "生命燃料消耗减少%d%%");

        add(Looting.COPPER_LOOTING_ITEM.get(), "抢夺升级 I");
        add(Looting.IRON_LOOTING_ITEM.get(), "抢夺升级 II");
        add(Looting.GOLD_LOOTING_ITEM.get(), "抢夺升级 III");
        add(Looting.DIAMOND_LOOTING_ITEM.get(), "抢夺升级 IV");
        add(Looting.NETHERITE_LOOTING_ITEM.get(), "抢夺升级 V");
        add("info.woot_revived.upgrade.looting.desc.0", "施加%d级抢夺效果");

        add(Mass.COPPER_MASS_ITEM.get(), "集群升级 I");
        add(Mass.IRON_MASS_ITEM.get(), "集群升级 II");
        add(Mass.GOLD_MASS_ITEM.get(), "集群升级 III");
        add(Mass.DIAMOND_MASS_ITEM.get(), "集群升级 IV");
        add(Mass.NETHERITE_MASS_ITEM.get(), "集群升级 V");
        add("info.woot_revived.upgrade.mass.desc.0", "击杀%d个生物");

        add(Rate.COPPER_RATE_ITEM.get(), "速率升级 I");
        add(Rate.IRON_RATE_ITEM.get(), "速率升级 II");
        add(Rate.GOLD_RATE_ITEM.get(), "速率升级 III");
        add(Rate.DIAMOND_RATE_ITEM.get(), "速率升级 IV");
        add(Rate.NETHERITE_RATE_ITEM.get(), "速率升级 V");
        add("info.woot_revived.upgrade.rate.desc.0", "所需时间减少%d%%");

        add(Decapitate.COPPER_DECAPITATE_ITEM.get(), "斩首升级 I");
        add(Decapitate.IRON_DECAPITATE_ITEM.get(), "斩首升级 II");
        add(Decapitate.GOLD_DECAPITATE_ITEM.get(), "斩首升级 III");
        add(Decapitate.DIAMOND_DECAPITATE_ITEM.get(), "斩首升级 IV");
        add(Decapitate.NETHERITE_DECAPITATE_ITEM.get(), "斩首升级 V");
        add("info.woot_revived.upgrade.decapitate.desc.0", "掉落%dx生物头颅");

        add(Xp.COPPER_XP_ITEM.get(), "经验升级 I");
        add(Xp.IRON_XP_ITEM.get(), "经验升级 II");
        add(Xp.GOLD_XP_ITEM.get(), "经验升级 III");
        add(Xp.DIAMOND_XP_ITEM.get(), "经验升级 IV");
        add(Xp.NETHERITE_XP_ITEM.get(), "经验升级 V");
        add("info.woot_revived.upgrade.xp.desc.0", "以碎片形式掉落%d%%经验");

        add(ShardDrop.IRON_SHARD_DROP_ITEM.get(), "碎片掉落升级 I");
        add(ShardDrop.GOLD_SHARD_DROP_ITEM.get(), "碎片掉落升级 II");
        add(ShardDrop.DIAMOND_SHARD_DROP_ITEM.get(), "碎片掉落升级 III");
        add(ShardDrop.NETHERITE_SHARD_DROP_ITEM.get(), "碎片掉落升级 IV");
        add("info.woot_revived.upgrade.shard_drop.desc.0", "掉落%s及所有低等级碎片");
        add("info.woot_revived.upgrade.shard_drop.desc.1", "最低等级要求：%s");
        add("info.woot_revived.upgrade.shard_drop.desc.2", "%d%%概率掉落碎片");

        add(Burn.BURN_ITEM.get(), "燃烧升级");
        add("info.woot_revived.upgrade.burn.desc.0", "击杀模拟生物时施加燃烧效果");

        add(Dimension.NETHER_DIMENSION_ITEM.get(), "下界维度升级");
        add(Dimension.END_DIMENSION_ITEM.get(), "末地维度升级");
        add("info.woot_revived.upgrade.dimension.desc.nether", "在下界击杀模拟生物");
        add("info.woot_revived.upgrade.dimension.desc.end", "在末地击杀模拟生物");

        add(ItemsRegistry.MOB_SHARD_ITEM.get(), "生物碎片");
        add(ItemsRegistry.MOB_SHARD_PROJECTILE.get(), "生物碎片");

        add(ItemsRegistry.XP_SHARD_ITEM.get(), "经验碎片");
        add(ItemsRegistry.XP_SPLINTER_ITEM.get(), "经验尖片");

        add(BlocksRegistry.FLUID_INFUSER_BLOCK.get(), "流体灌注器");

        add(ItemsRegistry.STYGIAN_INGOT_ITEM.get(), "幽冥锭");
        add(ItemsRegistry.STYGIAN_DUST_ITEM.get(), "幽冥粉");
        add(ItemsRegistry.STYGIAN_PLATE_ITEM.get(), "幽冥板");
        add(BlocksRegistry.STYGIAN_BLOCK.get(), "幽冥块");
        add(ItemsRegistry.PRISM_ITEM.get(), "棱镜");
        add(UpgradeItemsRegistry.UPGRADE_BASE_ITEM.get(), "升级基板");

        add(BlocksRegistry.ITEM_INFUSER_BLOCK.get(), "物品灌注器");
        add(ItemsRegistry.WHITE_DYE_CASING_ITEM.get(), "白色染料框架");
        add(ItemsRegistry.WHITE_DYE_PLATE_ITEM.get(), "白色染料板");
        add(ItemsRegistry.ORANGE_DYE_CASING_ITEM.get(), "橙色染料框架");
        add(ItemsRegistry.ORANGE_DYE_PLATE_ITEM.get(), "橙色染料板");
        add(ItemsRegistry.MAGENTA_DYE_CASING_ITEM.get(), "品红色染料框架");
        add(ItemsRegistry.MAGENTA_DYE_PLATE_ITEM.get(), "品红色染料板");
        add(ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM.get(), "淡蓝色染料框架");
        add(ItemsRegistry.LIGHT_BLUE_DYE_PLATE_ITEM.get(), "淡蓝色染料板");
        add(ItemsRegistry.YELLOW_DYE_CASING_ITEM.get(), "黄色染料框架");
        add(ItemsRegistry.YELLOW_DYE_PLATE_ITEM.get(), "黄色染料板");
        add(ItemsRegistry.LIME_DYE_CASING_ITEM.get(), "黄绿色染料框架");
        add(ItemsRegistry.LIME_DYE_PLATE_ITEM.get(), "黄绿色染料板");
        add(ItemsRegistry.PINK_DYE_CASING_ITEM.get(), "粉红色染料框架");
        add(ItemsRegistry.PINK_DYE_PLATE_ITEM.get(), "粉红色染料板");
        add(ItemsRegistry.GRAY_DYE_CASING_ITEM.get(), "灰色染料框架");
        add(ItemsRegistry.GRAY_DYE_PLATE_ITEM.get(), "灰色染料板");
        add(ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM.get(), "淡灰色染料框架");
        add(ItemsRegistry.LIGHT_GRAY_DYE_PLATE_ITEM.get(), "淡灰色染料板");
        add(ItemsRegistry.CYAN_DYE_CASING_ITEM.get(), "青色染料框架");
        add(ItemsRegistry.CYAN_DYE_PLATE_ITEM.get(), "青色染料板");
        add(ItemsRegistry.PURPLE_DYE_CASING_ITEM.get(), "紫色染料框架");
        add(ItemsRegistry.PURPLE_DYE_PLATE_ITEM.get(), "紫色染料板");
        add(ItemsRegistry.BLUE_DYE_CASING_ITEM.get(), "蓝色染料框架");
        add(ItemsRegistry.BLUE_DYE_PLATE_ITEM.get(), "蓝色染料板");
        add(ItemsRegistry.BROWN_DYE_CASING_ITEM.get(), "棕色染料框架");
        add(ItemsRegistry.BROWN_DYE_PLATE_ITEM.get(), "棕色染料板");
        add(ItemsRegistry.GREEN_DYE_CASING_ITEM.get(), "绿色染料框架");
        add(ItemsRegistry.GREEN_DYE_PLATE_ITEM.get(), "绿色染料板");
        add(ItemsRegistry.RED_DYE_CASING_ITEM.get(), "红色染料框架");
        add(ItemsRegistry.RED_DYE_PLATE_ITEM.get(), "红色染料板");
        add(ItemsRegistry.BLACK_DYE_CASING_ITEM.get(), "黑色染料框架");
        add(ItemsRegistry.BLACK_DYE_PLATE_ITEM.get(), "黑色染料板");

        add(ItemsRegistry.GUIDE_BOOK_ITEM.get(), "Woot指导书");

        add(BlocksRegistry.LAYOUT_BLOCK.get(), "工厂结构展示器");

        add(BlocksRegistry.DYE_LIQUIFIER_BLOCK.get(), "染料液化器");
        add(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK.get(), "附魔液化器");

        add(FluidsRegistry.VITALITY_FUEL_FLUID_BLOCK.get(), "生命燃料流体");
        add(FluidsRegistry.VITALITY_FUEL_FLUID_BUCKET.get(), "生命燃料桶");
        add("fluid_type.woot_revived." + FluidsRegistry.VITALITY_FUEL_FLUID_TAG, "生命燃料流体");

        add(FluidsRegistry.ENCHANTED_FLUID_BLOCK.get(), "附魔流体");
        add(FluidsRegistry.ENCHANTED_FLUID_BUCKET.get(), "附魔桶");
        add("fluid_type.woot_revived." + FluidsRegistry.ENCHANTED_FLUID_TAG, "附魔流体");

        add(FluidsRegistry.MOB_TEARS_FLUID_BLOCK.get(), "生物之泪流体");
        add(FluidsRegistry.MOB_TEARS_FLUID_BUCKET.get(), "生物之泪桶");
        add("fluid_type.woot_revived." + FluidsRegistry.MOB_TEARS_FLUID_TAG, "生物之泪流体");

        add(FluidsRegistry.PURE_DYE_FLUID_BLOCK.get(), "纯净染料流体");
        add(FluidsRegistry.PURE_DYE_FLUID_BUCKET.get(), "纯净染料桶");
        add("fluid_type.woot_revived." + FluidsRegistry.PURE_DYE_FLUID_TAG, "纯净染料流体");

        add("info.woot_revived.mobshard.unprogrammed.desc", "可通过攻击敌人，或右击敌人丢出碎片来捕获");
        add("info.woot_revived.mobshard.unprogrammed", "未编入");
        add("info.woot_revived.mobshard.remaining.desc", "击杀%s来编写碎片");
        add("info.woot_revived.mobshard.remaining.desc_no_entity", "击杀敌人来编写碎片");
        add("info.woot_revived.mobshard.remaining", "部分编入（击杀数：%d/%d）");
        add("info.woot_revived.mobshard.programmed", "编入完成");
        add("info.woot_revived.shard.0", "右击来获得经验");
        add("info.woot_revived.shard.1", "潜行右击来消耗整组");

        add("info.woot_revived.cell.amount", "生命燃料数量");
        add("info.woot_revived.tier", "等级");
        add("info.woot_revived.power", "能量");
        add("info.woot_revived.output_fluid", "输出流体");
        add("info.woot_revived.output_amount", "输出数量");
        add("info.woot_revived.output", "输出");
        add("info.woot_revived.input", "输入");
        add("info.woot_revived.augment_input", "补充物输入");
        add("info.woot_revived.input_fluid", "输入流体");
        add("info.woot_revived.input_amount", "输入数量");
        add("info.woot_revived.dye.red", "红色");
        add("info.woot_revived.dye.yellow", "黄色");
        add("info.woot_revived.dye.blue", "蓝色");
        add("info.woot_revived.dye.white", "白色");
        add("info.woot_revived.base_item", "基底物品");
        add("info.woot_revived.fluid", "流体");
        add("info.woot_revived.amount", "数量");
        add("info.woot_revived.progress", "进度");
        add("info.woot_revived.drained", "消耗总量");
        add("info.woot_revived.eta", "预计剩余时间");
        add("info.woot_revived.usage", "消耗");
        add("info.woot_revived.cost", "消耗");
        add("info.woot_revived.rate", "时间");
        add("info.woot_revived.direction", "方向");
        add("info.woot_revived.action", "行为");
        add("info.woot_revived.empty", "空");

        add("info.woot_revived.enchanted_liquifier.input", "附魔书输入");
        add("info.woot_revived.enchanted_liquifier.output", "附魔流体输出");
        add("info.woot_revived.dye_liquifier.input", "染料输入");
        add("info.woot_revived.dye_liquifier.output", "纯净染料输出");

        add("info.woot_revived.factory.invalid", "无效");
        add("info.woot_revived.factory.empty", "空");

        add("chat.woot_revived.anvil.cold", "幽冥砧必须置于岩浆块上");
        add("chat.woot_revived.anvil.nobase", "需先放置基底物品");

        add("misc.woot_revived.tier_1", "铜");
        add("misc.woot_revived.tier_2", "铁");
        add("misc.woot_revived.tier_3", "金");
        add("misc.woot_revived.tier_4", "钻石");
        add("misc.woot_revived.tier_5", "下界合金");

        add("gui.woot_revived.anvil.name", "幽冥砧");
        add("gui.woot_revived.enchanted_liquifier.name", "附魔液化器");
        add("gui.woot_revived.fluid_infuser.name", "流体灌注器");
        add("gui.woot_revived.item_infuser.name", "物品灌注器");
        add("gui.woot_revived.dye_liquifier.name", "染料液化器");
        add("gui.woot_revived.redstone.always_on", "红石模式：忽略");
        add("gui.woot_revived.redstone.with_no_signal", "红石模式：无红石信号时运行");
        add("gui.woot_revived.redstone.with_signal", "红石模式：有红石信号时运行");
        add("gui.woot_revived.redstone.once", "红石模式：按红石脉冲运行");
        add("gui.woot_revived.heart.name", "工厂核心");
        add("gui.woot_revived.heart.no_tier", "需搭建工厂");
        add("gui.woot_revived.heart.insufficient_tier", "需要%s等级");
        add("gui.woot_revived.heart.no_secondary", "未放置副伪刷怪笼");
        add("gui.woot_revived.heart.vitality_cell_missing", "缺失生命单元");
        add("gui.woot_revived.heart.vitality_cell", "生命单元");

        add("jei.woot_revived.shard", "由装有碎片掉落升级的工厂产出。");
        add("jei.woot_revived.anvil.0", "对幽冥砧右击来添加物品。");
        add("jei.woot_revived.anvil.1", "使用幽冥锤来进行合成。");
        add("jei.woot_revived.anvil.2", "幽冥砧必须放置在岩浆块上。");
        add("jei.woot_revived.anvil.3", "需要编入完成的生物碎片来获得伪刷怪笼方块。");
        add("jei.woot_revived.mob_shard.0", "使用碎片攻击生物，或者右击将碎片丢向生物来开始编入。");
        add("jei.woot_revived.mob_shard.1", "需要多次击杀同一种生物才能使碎片完全编入。");
        add("jei.woot_revived.mob_shard.2", "编入完成后，可在幽冥砧中将其转变为伪刷怪笼。");
        add("jei.woot_revived.pure_dye_fluid", "通过染料液化器产出，需要蓝色%dmB、黄色%dmB、红色%dmB，以及白色%dmB。");

        add("advancements.woot_revived.root.title", "Woot：重生");
        add("advancements.woot_revived.root.description", "你想念这个战利品模组了，对吧？");
        add("advancements.woot_revived.stygian_ingot.title", "一个锭要花多少！？");
        add("advancements.woot_revived.stygian_ingot.description", "获得你的首个幽冥锭");
        add("advancements.woot_revived.stygian_hammer.title", "你唯一需要的锤子！");
        add("advancements.woot_revived.stygian_hammer.description", "合成你的首个幽冥锤");
        add("advancements.woot_revived.stygian_anvil.title", "用生物的苦难塑造你的工厂！");
        add("advancements.woot_revived.stygian_anvil.description", "合成你的首个幽冥砧");
        add("advancements.woot_revived.shard_mold.title", "你最喜欢的模具！");
        add("advancements.woot_revived.shard_mold.description", "在幽冥砧上合成一个碎片模具");
        add("advancements.woot_revived.dye_mold.title", "你最讨厌的模具！");
        add("advancements.woot_revived.dye_mold.description", "在幽冥砧上合成一个染料模具");
        add("advancements.woot_revived.plate_mold.title", "又一个板模具？");
        add("advancements.woot_revived.plate_mold.description", "在幽冥砧上合成一个板模具");
        add("advancements.woot_revived.mob_shard.title", "绝对没有生物被武装！目移……");
        add("advancements.woot_revived.mob_shard.description", "在幽冥砧上通过碎片模具合成一个生物碎片");
        add("advancements.woot_revived.stygian_plate.title", "盛放在幽冥浅盘上");
        add("advancements.woot_revived.stygian_plate.description", "在幽冥砧上通过板模具合成一个幽冥板");
        add("advancements.woot_revived.factory_base.title", "你所有问题的根源");
        add("advancements.woot_revived.factory_base.description", "合成一个工厂基座");
        add("advancements.woot_revived.fluid_infuser.title", "你所有问题的根源");
        add("advancements.woot_revived.fluid_infuser.description", "合成一个工厂基座");
        add("advancements.woot_revived.mob_tears_bucket.title", "这些眼泪，享受啊！");
        add("advancements.woot_revived.mob_tears_bucket.description", "收集一桶生物之泪流体");
        add("advancements.woot_revived.vitality_fuel_bucket.title", "能从它们的泪水中获得生命燃料？？？");
        add("advancements.woot_revived.vitality_fuel_bucket.description", "收集一桶生命燃料流体");
        add("advancements.woot_revived.enchanted_copper_plate.title", "闪亮铜板，呃……");
        add("advancements.woot_revived.enchanted_copper_plate.description", "合成一个附魔铜板");
        add("advancements.woot_revived.copper_cell.title", "这储罐太小了……");
        add("advancements.woot_revived.copper_cell.description", "合成一个铜单元");
        add("advancements.woot_revived.enchanted_iron_plate.title", "闪亮铁板，不错。");
        add("advancements.woot_revived.enchanted_iron_plate.description", "合成一个附魔铁板");
        add("advancements.woot_revived.iron_cell.title", "这储罐开始感觉不错了。");
        add("advancements.woot_revived.iron_cell.description", "合成一个铁单元");
        add("advancements.woot_revived.enchanted_gold_plate.title", "闪亮金板，哇哦！");
        add("advancements.woot_revived.enchanted_gold_plate.description", "合成一个附魔金板");
        add("advancements.woot_revived.gold_cell.title", "这储罐闪亮亮的真不错！");
        add("advancements.woot_revived.gold_cell.description", "合成一个金单元");
        add("advancements.woot_revived.enchanted_diamond_plate.title", "闪亮钻石板，太富啦！");
        add("advancements.woot_revived.enchanted_diamond_plate.description", "合成一个附魔钻石板");
        add("advancements.woot_revived.diamond_cell.title", "这储罐看起来真豪华！");
        add("advancements.woot_revived.diamond_cell.description", "合成一个钻石单元");
        add("advancements.woot_revived.enchanted_netherite_plate.title", "闪亮下界合金板，酷毙了！！！");
        add("advancements.woot_revived.enchanted_netherite_plate.description", "合成一个附魔下界合金板");
        add("advancements.woot_revived.netherite_cell.title", "这储罐简直就是从地狱里出来的！！！");
        add("advancements.woot_revived.netherite_cell.description", "合成一个下界合金单元");
        add("advancements.woot_revived.enchanted_liquifier.title", "你想要那些亮闪闪的玩意对吧？");
        add("advancements.woot_revived.enchanted_liquifier.description", "合成一个附魔液化器");
        add("advancements.woot_revived.enchanted_bucket.title", "你得到了个闪亮的桶！");
        add("advancements.woot_revived.enchanted_bucket.description", "收集一桶附魔流体");
        add("advancements.woot_revived.item_infuser.title", "你之后会灌注些物品的！");
        add("advancements.woot_revived.item_infuser.description", "合成一个物品灌注器");
        add("advancements.woot_revived.dye_liquifier.title", "把所有颜色混一块儿不觉得糟糕吗？");
        add("advancements.woot_revived.dye_liquifier.description", "合成一个染料液化器");
        add("advancements.woot_revived.pure_dye.title", "结果就是彩虹！！！");
        add("advancements.woot_revived.pure_dye.description", "收集一桶纯净染料流体");
        add("advancements.woot_revived.prism.title", "这块玻璃能反射所有颜色！");
        add("advancements.woot_revived.prism.description", "通过物品灌注器，使用玻璃和纯净染料流体合成一个棱镜");
        add("advancements.woot_revived.fake_spawner.title", "这种刷怪笼真的存在吗？");
        add("advancements.woot_revived.fake_spawner.description", "合成一个伪刷怪笼");
        add("advancements.woot_revived.black_dye_plate.title", "开启升级之旅！");
        add("advancements.woot_revived.black_dye_plate.description", "合成一个黑色染料板");
        add("advancements.woot_revived.upgrade_base.title", "你的升级归属之处！");
        add("advancements.woot_revived.upgrade_base.description", "合成一个升级基板");
        add("advancements.woot_revived.copper_shard.title", "锈蚀碎片！");
        add("advancements.woot_revived.copper_shard.description", "在幽冥砧上合成一个铜碎片");
        add("advancements.woot_revived.copper_pylon.title", "你的首个塔柱！");
        add("advancements.woot_revived.copper_pylon.description", "合成一个铜塔柱");
        add("advancements.woot_revived.copper_plinth.title", "你的首个柱基！");
        add("advancements.woot_revived.copper_plinth.description", "合成一个铜柱基");
        add("advancements.woot_revived.iron_shard_upgrade.title", "金属碎片升级！");
        add("advancements.woot_revived.iron_shard_upgrade.description", "合成铁碎片升级");
        add("advancements.woot_revived.iron_shard.title", "金属碎片！");
        add("advancements.woot_revived.iron_shard.description", "通过工厂获得一个铁碎片");
        add("advancements.woot_revived.iron_pylon.title", "你的第二个塔柱！");
        add("advancements.woot_revived.iron_pylon.description", "合成一个铁塔柱");
        add("advancements.woot_revived.iron_plinth.title", "你的第二个柱基！");
        add("advancements.woot_revived.iron_plinth.description", "合成一个铁柱基");
        add("advancements.woot_revived.gold_shard_upgrade.title", "闪亮碎片升级！");
        add("advancements.woot_revived.gold_shard_upgrade.description", "合成金碎片升级");
        add("advancements.woot_revived.gold_shard.title", "闪亮碎片！");
        add("advancements.woot_revived.gold_shard.description", "通过工厂获得一个金碎片");
        add("advancements.woot_revived.gold_pylon.title", "你的第三个塔柱！");
        add("advancements.woot_revived.gold_pylon.description", "合成一个金塔柱");
        add("advancements.woot_revived.gold_plinth.title", "你的第三个柱基！");
        add("advancements.woot_revived.gold_plinth.description", "合成一个金柱基");
        add("advancements.woot_revived.diamond_shard_upgrade.title", "富裕碎片升级！");
        add("advancements.woot_revived.diamond_shard_upgrade.description", "合成钻石碎片升级");
        add("advancements.woot_revived.diamond_shard.title", "富裕碎片！");
        add("advancements.woot_revived.diamond_shard.description", "通过工厂获得一个钻石碎片");
        add("advancements.woot_revived.diamond_pylon.title", "你的第四个塔柱！");
        add("advancements.woot_revived.diamond_pylon.description", "合成一个钻石塔柱");
        add("advancements.woot_revived.diamond_plinth.title", "你的第四个柱基！");
        add("advancements.woot_revived.diamond_plinth.description", "合成一个钻石柱基");
        add("advancements.woot_revived.netherite_shard_upgrade.title", "地狱碎片升级！");
        add("advancements.woot_revived.netherite_shard_upgrade.description", "合成下界合金碎片升级");
        add("advancements.woot_revived.netherite_shard.title", "地狱碎片！");
        add("advancements.woot_revived.netherite_shard.description", "通过工厂获得一个下界合金碎片");
        add("advancements.woot_revived.netherite_pylon.title", "你的第五个也是最后一个塔柱！");
        add("advancements.woot_revived.netherite_pylon.description", "合成一个下界合金塔柱");
        add("advancements.woot_revived.netherite_plinth.title", "你的第五个也是最后一个柱基！");
        add("advancements.woot_revived.netherite_plinth.description", "合成一个下界合金柱基");
    }
}